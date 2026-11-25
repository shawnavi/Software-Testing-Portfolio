import com.team.expensetracker.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * Web front end for the Expense Tracking and Personal Finance Management System.
 * Every action calls the same classes that the JUnit tests check (build v1.1).
 *
 * Run from the repository root:
 *   mvn -q -f 05_Test_Automation/expense-tracker/pom.xml compile
 *   java -cp 05_Test_Automation/expense-tracker/target/classes 08_Frontend/WebApp.java
 * Then open http://localhost:8090
 */
public class WebApp {
    static final int PORT = 8090;
    static final String DEMO_USER = "student01";
    static final String DEMO_PASSWORD = "Budget@2026";

    static ExpenseTracker tracker;
    static BudgetManager budgets;
    static ReportService reports;
    static UserAccount account;
    static final Map<String, BigDecimal> budgetLimits = new TreeMap<>();
    static boolean alertsOn = true;

    public static void main(String[] args) throws IOException {
        tracker = new ExpenseTracker(new ExpenseValidator(Clock.systemDefaultZone()));
        budgets = new BudgetManager(tracker);
        reports = new ReportService(tracker);
        account = new UserAccount(DEMO_USER, DEMO_PASSWORD);
        seed();

        // Works whether started from the repository root or from inside 08_Frontend
        Path page = Path.of("08_Frontend", "index.html");
        final Path indexPage = Files.exists(page) ? page : Path.of("index.html");

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", PORT), 0);
        server.createContext("/", ex -> {
            if (!ex.getRequestURI().getPath().equals("/")) {
                send(ex, 404, "text/plain", "Not found");
                return;
            }
            send(ex, 200, "text/html; charset=utf-8", Files.readString(indexPage));
        });
        server.createContext("/api/", WebApp::api);
        server.start();
        System.out.println("Expense Tracker running at http://localhost:" + PORT);
        System.out.println("Demo login: " + DEMO_USER + " / " + DEMO_PASSWORD);
    }

    static void seed() {
        YearMonth now = YearMonth.now();
        YearMonth prev = now.minusMonths(1);
        LocalDate today = LocalDate.now();
        add("12000.00", Category.RENT, "Hostel rent", prev.atDay(1));
        add("3500.50", Category.FOOD, "Groceries", prev.atDay(5));
        add("1499.50", Category.FOOD, "Restaurant", prev.atEndOfMonth());
        add("800.00", Category.TRANSPORT, "Metro card", prev.atDay(10));
        add("12000.00", Category.RENT, "Hostel rent", now.atDay(1));
        add("2450.00", Category.FOOD, "Groceries", now.atDay(Math.min(4, today.getDayOfMonth())));
        add("650.00", Category.TRANSPORT, "Bus pass", now.atDay(Math.min(6, today.getDayOfMonth())));
        add("499.00", Category.ENTERTAINMENT, "Movie night", now.atDay(Math.min(12, today.getDayOfMonth())));
        setBudget(Category.FOOD, now, new BigDecimal("3000.00"));
        setBudget(Category.TRANSPORT, now, new BigDecimal("2000.00"));
        setBudget(Category.ENTERTAINMENT, now, new BigDecimal("500.00"));
    }

    static void add(String amount, Category c, String d, LocalDate date) {
        tracker.addExpense(new BigDecimal(amount), c, d, date);
    }

    static void setBudget(Category c, YearMonth m, BigDecimal limit) {
        budgets.setBudget(c, m, limit);
        budgetLimits.put(c + "@" + m, limit);
    }

    // ---------------------------------------------------------------- API

    static void api(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath().substring("/api/".length());
        String method = ex.getRequestMethod();
        Map<String, String> q = parse(ex.getRequestURI().getRawQuery());
        if (!method.equals("GET")) {
            q.putAll(parse(new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8)));
        }
        try {
            String body = switch (method + " " + path) {
                case "GET account" -> accountJson();
                case "POST login" -> {
                    boolean ok = account.login(q.get("password"));
                    yield "{\"ok\":" + ok + ",\"account\":" + accountJson() + "}";
                }
                case "POST logout" -> { account.logout(); yield accountJson(); }
                case "POST unlock" -> { account.unlock(); yield accountJson(); }
                default -> { requireLogin(); yield secured(method, path, q); }
            };
            send(ex, 200, "application/json", body);
        } catch (ValidationException | IllegalStateException | IllegalArgumentException e) {
            send(ex, 400, "application/json", "{\"error\":" + str(e.getMessage()) + "}");
        } catch (SecurityException e) {
            send(ex, 401, "application/json", "{\"error\":" + str(e.getMessage()) + "}");
        }
    }

    static void requireLogin() {
        if (account.getState() != AccountState.LOGGED_IN) {
            throw new SecurityException("Please log in first");
        }
    }

    static String secured(String method, String path, Map<String, String> q) {
        if (method.equals("GET") && path.equals("expenses")) {
            List<Expense> list;
            if (q.containsKey("from") && q.containsKey("to")) {
                list = tracker.findBetween(LocalDate.parse(q.get("from")), LocalDate.parse(q.get("to")));
            } else {
                list = tracker.getAll();
            }
            String cat = q.get("category");
            if (cat != null && !cat.isEmpty()) {
                Category c = Category.valueOf(cat);
                list = list.stream().filter(e -> e.category() == c).toList();
            }
            return expensesJson(list);
        }
        if (method.equals("POST") && path.equals("expenses")) {
            Expense e = tracker.addExpense(amount(q.get("amount")), category(q.get("category")),
                    q.get("description"), date(q.get("date")));
            return expenseJson(e);
        }
        if (method.equals("POST") && path.startsWith("expenses/") && path.endsWith("/edit")) {
            int id = Integer.parseInt(path.split("/")[1]);
            Expense e = tracker.updateExpense(id, amount(q.get("amount")), category(q.get("category")),
                    q.get("description"), date(q.get("date")));
            return expenseJson(e);
        }
        if (method.equals("POST") && path.startsWith("expenses/") && path.endsWith("/delete")) {
            tracker.deleteExpense(Integer.parseInt(path.split("/")[1]));
            return "{\"ok\":true}";
        }
        if (method.equals("GET") && path.equals("budgets")) {
            return budgetsJson(month(q.get("month")));
        }
        if (method.equals("POST") && path.equals("budgets")) {
            setBudget(category(q.get("category")), month(q.get("month")), amount(q.get("limit")));
            return budgetsJson(month(q.get("month")));
        }
        if (method.equals("POST") && path.equals("alerts")) {
            alertsOn = Boolean.parseBoolean(q.get("enabled"));
            budgets.setAlertsEnabled(alertsOn);
            return budgetsJson(month(q.get("month")));
        }
        if (method.equals("GET") && path.equals("report")) {
            YearMonth m = month(q.get("month"));
            BigDecimal income = amountOrZero(q.get("income"));
            StringBuilder sb = new StringBuilder("{\"month\":" + str(m.toString()));
            sb.append(",\"total\":").append(str(reports.monthlyTotal(m).toPlainString()));
            sb.append(",\"income\":").append(str(income.toPlainString()));
            sb.append(",\"savings\":").append(str(reports.savings(income, m).toPlainString()));
            sb.append(",\"savingsRate\":").append(str(reports.savingsRate(income, m).toPlainString()));
            sb.append(",\"breakdown\":[");
            String sep = "";
            for (var en : reports.categoryBreakdown(m).entrySet()) {
                sb.append(sep).append("{\"category\":").append(str(en.getKey().name()))
                  .append(",\"amount\":").append(str(en.getValue().toPlainString())).append("}");
                sep = ",";
            }
            return sb.append("]}").toString();
        }
        throw new IllegalArgumentException("Unknown request: " + method + " /api/" + path);
    }

    // ---------------------------------------------------------------- helpers

    static BigDecimal amount(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return new BigDecimal(s.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Amount must be a number");
        }
    }

    static BigDecimal amountOrZero(String s) {
        BigDecimal v = amount(s);
        return v == null ? BigDecimal.ZERO : v;
    }

    static Category category(String s) {
        return (s == null || s.isBlank()) ? null : Category.valueOf(s);
    }

    static LocalDate date(String s) {
        return (s == null || s.isBlank()) ? null : LocalDate.parse(s);
    }

    static YearMonth month(String s) {
        return (s == null || s.isBlank()) ? YearMonth.now() : YearMonth.parse(s);
    }

    static String accountJson() {
        return "{\"username\":" + str(account.getUsername()) + ",\"state\":" + str(account.getState().name())
                + ",\"failedAttempts\":" + account.getFailedAttempts()
                + ",\"maxAttempts\":" + UserAccount.MAX_FAILED_ATTEMPTS + "}";
    }

    static String expensesJson(List<Expense> list) {
        StringJoiner j = new StringJoiner(",", "[", "]");
        list.forEach(e -> j.add(expenseJson(e)));
        return j.toString();
    }

    static String expenseJson(Expense e) {
        return "{\"id\":" + e.id() + ",\"amount\":" + str(e.amount().toPlainString())
                + ",\"category\":" + str(e.category().name()) + ",\"description\":" + str(e.description())
                + ",\"date\":" + str(e.date().toString()) + "}";
    }

    static String budgetsJson(YearMonth m) {
        StringJoiner j = new StringJoiner(",", "[", "]");
        for (Category c : Category.values()) {
            BigDecimal limit = budgetLimits.get(c + "@" + m);
            BigDecimal spent = budgets.spent(c, m);
            if (limit == null && spent.signum() == 0) continue;
            j.add("{\"category\":" + str(c.name())
                    + ",\"limit\":" + (limit == null ? "null" : str(limit.toPlainString()))
                    + ",\"spent\":" + str(spent.toPlainString())
                    + ",\"percent\":" + str(budgets.percentUsed(c, m).toPlainString())
                    + ",\"status\":" + str(budgets.getStatus(c, m).name())
                    + ",\"alert\":" + budgets.shouldSendAlert(c, m) + "}");
        }
        return "{\"month\":" + str(m.toString()) + ",\"alertsOn\":" + alertsOn + ",\"items\":" + j + "}";
    }

    static Map<String, String> parse(String raw) {
        Map<String, String> m = new HashMap<>();
        if (raw == null || raw.isEmpty()) return m;
        for (String pair : raw.split("&")) {
            int i = pair.indexOf('=');
            String k = URLDecoder.decode(i < 0 ? pair : pair.substring(0, i), StandardCharsets.UTF_8);
            String v = i < 0 ? "" : URLDecoder.decode(pair.substring(i + 1), StandardCharsets.UTF_8);
            m.put(k, v);
        }
        return m;
    }

    static String str(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        return sb.append('"').toString();
    }

    static void send(HttpExchange ex, int code, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", type);
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
