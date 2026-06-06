<div align="center">
  <img src="https://raw.githubusercontent.com/mr-mingo/SNI-Spoofing-Android/refs/heads/main/app/src/main/res/drawable/app_icon_sp_1780550720701.png" width="180" style="border-radius: 20px;" alt="SNI Spoofing Logo" />
  <h1>SNI-Spoofing-Android</h1>
  <p>🚀 یک کلاینت قدرتمند، سبک و متن‌باز برای دور زدن فیلترینگ و DPI از طریق جعل SNI و فرگمنتیشن در اندروید</p>

  <p>
    <a href="https://github.com/mr-mingo/SNI-Spoofing-Android/releases"><img src="https://img.shields.io/badge/Release-v0.9.0-38BDF8?style=for-the-badge" alt="Release"></a>
    <a href="https://github.com/mr-mingo/SNI-Spoofing-Android/stargazers"><img src="https://img.shields.io/github/stars/mr-mingo/SNI-Spoofing-Android?color=EAB308&label=Stars&logo=github&style=for-the-badge" alt="Stars"></a>
  </p>

  <p>
    <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square" alt="License: MIT"></a>
    <a href="https://developer.android.com"><img src="https://img.shields.io/badge/Platform-Android-green.svg?logo=android&style=flat-square" alt="Android Platform"></a>
    <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-1.9+-purple.svg?logo=kotlin&style=flat-square" alt="Kotlin"></a>
  </p>
</div>

---

<div dir="rtl" style="text-align: right;">

<h2 style="text-align: right;">🌟 معرفی پروژه</h2>
<p style="text-align: right;">
<strong>SNI-Spoofing-Android</strong> یک اپلیکیشن بومی و اوپن‌سورس اندرویدی است که با ایجاد یک تونل پروکسی محلی (Local Proxy) و استفاده از تکنیک‌های پیشرفته خرد کردن بسته‌ها (TCP Fragmentation) و جعل نام سرور (SNI Spoofing)، به شما اجازه می‌دهد از سد سیستم‌های تحلیل‌گر عمیق بسته‌ها (DPI) عبور کنید.
</p>
<p style="text-align: right;">
این برنامه بدون نیاز به دسترسی روت (Root) کار می‌کند و ابزاری عالی برای استفاده در کنار کلاینت‌هایی مانند Hiddify یا v2rayNG محسوب می‌شود.
</p>

---

<h2 style="text-align: right;">✨ ویژگی‌های کلیدی</h2>
<ul style="text-align: right; direction: rtl;">
  <li>🛡️ <strong>عبور از DPI:</strong> دور زدن محدودیت‌های فیلترینگ مبتنی بر SNI با مکانیسم‌های قطعه‌قطعه کردن بسته‌های اتمیک شبکه (TCP Fragmentation).</li>
  <li>🧵 <strong>هسته همزمانی و مالتی‌تردینگ بهینه:</strong> لایه سوکت بازنویسی‌شده با ساختار استخر نخ‌های کش‌شده جهت هندل کردن نامحدود اتصالات موازی و همزمان در برنامه‌های سنگین بدون قفل شدن هسته پروکسی.</li>
  <li>🔋 <strong>مدیریت هوشمند منابع و باتری:</strong> راندمان مصرف انرژی فوق‌العاده بالا به دلیل بستن آنی اتصالات بلاتکلیف و به حداقل رساندن چرخه‌های پردازشی هرز در پس‌زمینه.</li>
  <li>⚡ <strong>پروکسی لوکال (Loopback):</strong> بدون تداخل با VPN‌های سیستم کار می‌کند و به صورت یک پروکسی واسط در پورت مشخص روی آدرس لوکال (<code>127.0.0.1</code>) اجرا می‌شود.</li>
  <li>🎨 <strong>رابط کاربری مدرن:</strong> طراحی چشم‌نواز، مینیمال و کاربرپسند توسعه یافته با Jetpack Compose (Material 3).</li>
  <li>📉 <strong>مانیتورینگ زنده:</strong> مشاهده وضعیت ترافیک مصرفی، اتصالات فعال و تاریخچه لاگ‌ها در لحظه (Real-time).</li>
  <li>🎛️ <strong>شورتکات تنظیمات سریع (QS Tile):</strong> امکان خاموش/روشن کردن پروکسی از طریق کنترل سنتر اندروید (Quick Settings).</li>
</ul>

---

<h2 style="text-align: right;">📥 روند انتشار نسخه‌ها و نصب</h2>
<p style="text-align: right;">ما همواره از سیستم انتشار بومی گیت‌هاب برای ارائه‌ی نسخه‌های برنامه استفاده کرده‌ایم:</p>
<ul style="text-align: right; direction: rtl;">
  <li><strong>نسخه‌های قدیمی (پیش از <code>v0.9.0</code>):</strong> این نسخه‌ها فاقد بهینه‌سازی‌های مدیریت ریسمان (Threads) و حافظه بوده‌اند و به دلیل پدیده اشباع استخر اتصالات، پایداری لازم را در ترافیک‌های سنگین ندارند؛ لذا استفاده از آن‌ها به هیچ وجه توصیه نمی‌شود.</li>
  <li><strong>نسخه‌های پایدار و بهینه (نسخه <code>v0.9.0</code> و بالاتر):</strong> شامل آخرین متدهای مدیریت پویا، ساختار مالتی‌تردینگِ کش‌شده و بهینه‌سازی‌های جامع باتری هستند.</li>
</ul>
<p style="text-align: right;">
تأکید می‌شود که همیشه جدیدترین نسخه پایدار را دریافت کنید. بدین منظور به بخش <a href="https://github.com/mr-mingo/SNI-Spoofing-Android/releases"><strong>Releases</strong></a> در همین مخزن بروید، آخرین فایل <code>.apk</code> را دانلود و روی گوشی خود نصب کنید.
</p>

---

<h2 style="text-align: right;">🛠 ابعاد فنی و معماری</h2>
<p style="text-align: right;">
این برنامه با عبور از لایه‌های محدودکننده استاندارد اندروید طراحی شده است. در نسخه‌های اخیر، هسته سوکت برنامه از لایه دیسپچر خروجی/ورودی کوروتین (مانند <code>Dispatchers.IO</code> با سقف محدود ۶۴ نخ پردازشی) فراتر رفته و به ساختار <code>Cached Thread Pool</code> مجهز شده است تا با تخصیص داینامیک ریسمان مستقل به هر سوکتِ پذیرفته‌شده، مانع از پدیده گرسنگی نخ‌ها (Thread Starvation) و قفل شدن کل اپلیکیشن در درخواست‌های همزمان بالا (مانند اسکرول اینستاگرام) شود.
</p>
<p style="text-align: right;"><strong>کتابخانه‌ها و فریم‌ورک‌های مورد استفاده:</strong></p>
<ul style="text-align: right; direction: rtl;">
  <li><strong>Kotlin (Coroutines & Executors):</strong> مدیریت ریسمان‌ها در سطح بومی سیستم‌عامل و پیاده‌سازی اتصالات ناهمگام غیرانسدادی با ترکیب Coroutines و استخرهای سفارشی جاوا.</li>
  <li><strong>Jetpack Compose:</strong> فریم‌ورک اصلی برای ساخت رابط کاربری (UI) مدرن، دوزبانه و واکنشی بدون استفاده از لایه‌های سنگین XML.</li>
  <li><strong>Android Service & Foreground Notifications:</strong> جهت تداوم فعالیت پروکسی لوکال در پس‌زمینه و جلوگیری از کشته شدن پروسس شبکه توسط سیستم مدیریت حافظه اندروید.</li>
  <li><strong>TileService API:</strong> همگام‌سازی کاشی منوی تنظیمات سریع با ساختار وکتور تطبیق‌پذیر با تم سیستمی دستگاه.</li>
  <li><strong>Low-Level Network Sockets:</strong> پیاده‌سازی مستقیم سوکت‌های سطح پایین پروتکل TCP برای شنود، رله ترافیک و تزریق قطعات فرگمنتیشن در طول دست‌تکانی TLS.</li>
</ul>

---

<h2 style="text-align: right;">🚀 نصب و راه‌اندازی (Development)</h2>
<p style="text-align: right;">برای اجرای سورس کد روی ماشین توسعه خودت، مراحل زیر را طی کن:</p>

```bash
# 1. مخزن را کلون کنید
git clone [https://github.com/mr-mingo/SNI-Spoofing-Android.git](https://github.com/mr-mingo/SNI-Spoofing-Android.git)

# 2. وارد پوشه پروژه شوید
cd SNI-Spoofing-Android

# 3. با استفاده از گریدل پروژه را بیلد کنید
# اطمینان حاصل کنید که جاوا 11+ یا جدیدتر روی سیستم نصب باشد
./gradlew assembleDebug

```

**ETH, BNB, MATIC network (ERC20, BEP20):**

```text
0x9391475e4606322dc4a35daeef2da16910862afb

```

**TRON network (TRC20):**

```text
TSKr7K827wsDchiXiDhKS5J1bU1fN4AVF3

```

**Bitcoin network:**

```text
bc1q4jdhy3j2egfrm36pgv8f8senlesm5edvhuxuy9

```

**Dogecoin network:**

```text
DDDn4kCN9P3ThrHALrV8WnrYxfoMv6nrUK

```

**TON network:**

```text
UQCPjuSUe5OADe8ZkN_RDkEsmcff0jWgrmpKKoxfo8C0CVBN

```

---

## 🌟 Introduction

**SNI-Spoofing-Android** is a native, open-source Android application that creates a local proxy tunnel to help you bypass Deep Packet Inspection (DPI) and SNI-based filtering. It uses advanced TCP fragmentation techniques to spoof the Server Name Indicator during the TLS handshake.
The application operates entirely without root permissions and perfectly pairs as a routing overlay for clients like Hiddify or v2rayNG.

---

## ✨ Key Features

* 🛡️ **DPI Evasion:** Overcome SNI-based filtering limitations using robust TCP fragmentation mechanisms.
* 🧵 **High-Performance Multi-threading:** Powered by a customized cached thread pool architecture, avoiding core socket freezes and handling mass parallel connection overhead efficiently.
* 🔋 **Advanced Battery Management:** High energy efficiency through rapid lifecycle tracking and immediate termination of stale or dangling sockets.
* ⚡ **Local Proxy (Loopback):** Operates on `127.0.0.1` and runs non-intrusively without touching system VpnService, ensuring it doesn't conflict with other VPN tunnels.
* 🎨 **Modern Interface:** Experience a beautifully crafted, minimal, and user-friendly UI built natively using Jetpack Compose (Material 3).
* 📉 **Real-time Monitoring:** Keep track of live traffic streams, transmitted bytes, and active connection flows through the interactive dashboard.
* 🎛️ **Quick Settings (QS) Tile:** Start or stop the tunneling process directly from the Android control center.

---

## 📥 Release Cycle & Installation

We utilize GitHub Releases to distribute our application binaries:

* **Old Versions (prior to `v0.9.0`):** These builds lack fundamental threading optimizations and can suffer from connection pool exhaustion under heavy concurrency loads. Their usage is highly discouraged.
* **Stable & Optimized Releases (`v0.9.0+`):** Production-ready, fully verified stable APKs with refined low-level network concurrency and highly reduced power consumption.

Go to the Releases section, download the latest `.apk` file, and install it on your Android device.

---

## 🛠 Technical Architecture

The core network loop has been heavily re-engineered to bypass standard Android coroutine limitations. Instead of using default coroutine dispatchers (such as `Dispatchers.IO`, which caps execution at 64 parallel threads), the networking core leverages an asynchronous Java-backed `Cached Thread Pool` via `Executors`. This ensures dynamic thread allocation for every accepted socket, completely eliminating thread starvation and app freezes during rapid sequential content loading (e.g., streaming or downloading).

**Libraries & Technologies used:**

* **Kotlin (Coroutines & Executors):** Core backend implementation combining standard coroutines with dynamic Java low-level executors for thread-safe concurrent socket parsing.
* **Jetpack Compose:** The prominent Android UI toolkit for constructing an interactive, reactive frontend layout.
* **Android Native Services:** Leveraging Foreground Services and Notifications to maintain sustained socket activity and network relays without aggressive system kills.
* **TileService:** Deeply integrated with the Android infrastructure to provide a functional QS Menu shortcut tool.
* **Custom Sockets:** Lower-level Java networking Socket implementation to process TCP streams and execute customized fragmentation logic in transit.

---

## 🚀 Installation & Setup

To build and test the codebase locally, run the standard Gradle procedures:

```bash
# 1. Clone the repository
git clone [https://github.com/mr-mingo/SNI-Spoofing-Android.git](https://github.com/mr-mingo/SNI-Spoofing-Android.git)

# 2. Change directory
cd SNI-Spoofing-Android

# 3. Assemble the project 
# Ensure Java 11+ or newer is installed in your environment
./gradlew assembleDebug

```

You can install the generated artifact directly from the `app/build/outputs/apk/debug/` directory to your connected target device.

---

## 💡 How it Works

1. Launch the app and tap **Start**.
2. A local intermediate proxy will listen on `127.0.0.1` (default port `40443`).
3. Inside your primary VPN client (like Hiddify or v2rayNG), configure the outbound route or direct server proxy target to point toward `127.0.0.1` at the designated port.
4. Your connection flows outbound entirely through this application, where TLS handshakes are fragmented intentionally to bypass ISP-level Deep Packet Inspection firewalls before routing externally to the actual target servers.

---

## 🗺 Roadmap

* [x] **v0.1.0 - v0.4.0:** Core Kotlin architecture, local proxy service integration, and basic fragmentation backend.
* [x] **v0.5.0 - v0.8.0:** Live connection dashboard, Material 3 UI enhancement via Jetpack Compose, and robust log parsing.
* [x] **v0.9.x:** Integrated Quick Settings Tile, battery optimizations, and custom brand identity icon.
* [ ] **v1.0.0 (Stable):** Official first stable deployment made available under the Releases block.

---

## 📚 References

* Core bypass logic is highly inspired by and structurally sourced from: [patterniha/SNI-Spoofing](https://github.com/patterniha/SNI-Spoofing) - Heartfelt thanks to the original developer for their amazing work!

---

## ⚖️ License

This project is released under the **MIT License**. It is fully open-source and freely available for modifications.

---

## ☕ Donation

If you found this project useful and would like to support its development, you can make a donation in one of the following crypto networks:

**ETH, BNB, MATIC network (ERC20, BEP20):**

```text
0x9391475e4606322dc4a35daeef2da16910862afb

```

**TRON network (TRC20):**

```text
TSKr7K827wsDchiXiDhKS5J1bU1fN4AVF3

```

**Bitcoin network:**

```text
bc1q4jdhy3j2egfrm36pgv8f8senlesm5edvhuxuy9

```

**Dogecoin network:**

```text
DDDn4kCN9P3ThrHALrV8WnrYxfoMv6nrUK

```

**TON network:**

```text
UQCPjuSUe5OADe8ZkN_RDkEsmcff0jWgrmpKKoxfo8C0CVBN

```

Thank you for your support! 🙏

```

```
