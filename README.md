<div align="center">
  <img src="https://raw.githubusercontent.com/mr-mingo/SNI-Spoofing-Android/refs/heads/main/app/src/main/res/drawable/app_icon_sp_1780550720701.png" width="360" style="border-radius: 40px;" alt="SNI Spoofing Logo" />
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

<h2 style="text-align: right;">🌟 معرفی</h2>
<p style="text-align: right;">
<strong>SNI-Spoofing-Android</strong> یک اپلیکیشن بومی و متن‌باز اندرویدی است که با ایجاد یک تونل پروکسی محلی به شما در دور زدن سیستم‌های تحلیل عمیق بسته‌ها (DPI) و فیلترینگ مبتنی بر SNI کمک می‌کند. این برنامه از تکنیک‌های پیشرفته خرد کردن بسته‌های TCP (فرگمنتیشن) برای جعل نام سرور (SNI) در طول دست‌تکانی TLS استفاده می‌کند. این اپلیکیشن کاملاً بدون نیاز به دسترسی روت کار می‌کند و به عنوان یک لایه مسیریابی مکمل در کنار کلاینت‌هایی مانند Hiddify یا v2rayNG به طور بی‌نقص جفت می‌شود.
</p>

<h2 style="text-align: right;">✨ ویژگی‌های کلیدی</h2>
<ul style="text-align: right; direction: rtl;">
  <li>🛡️ <strong>دور زدن DPI:</strong> غلبه بر محدودیت‌های فیلترینگ مبتنی بر SNI با استفاده از مکانیسم‌های قدرتمند خرد کردن بسته‌های TCP.</li>
  <li>🧵 <strong>مالتی‌تردینگ با کارایی بالا:</strong> قدرت گرفته از معماری استخر نخ‌های کش‌شده سفارشی (Cached Thread Pool)، جهت جلوگیری از قفل شدن سوکت‌های اصلی و مدیریت کارآمد بار اضافی اتصالات موازی انبوه.</li>
  <li>🔋 <strong>مدیریت پیشرفته باتری:</strong> راندمان مصرف انرژی بالا از طریق ردیابی سریع چرخه حیات و قطع فوری سوکت‌های بلاتکلیف یا رها شده.</li>
  <li>⚡ <strong>پروکسی لوکال (Loopback):</strong> روی <code>127.0.0.1</code> عمل می‌کند و به صورت غیرتهاجمی بدون دستکاری VpnService سیستم اجرا می‌شود تا تضمین کند هیچ تداخلی با سایر تونل‌های VPN ایجاد نمی‌کند.</li>
  <li>🎨 <strong>رابط کاربری مدرن:</strong> تجربه یک رابط کاربری (UI) زیبا، مینیمال و کاربرپسند که به صورت بومی با استفاده از Jetpack Compose (Material 3) ساخته شده است.</li>
  <li>📉 <strong>مانیتورینگ زنده:</strong> ردیابی لحظه‌ای جریان ترافیک زنده، بایت‌های منتقل شده و جریان اتصالات فعال از طریق داشبورد تعاملی.</li>
  <li>🎛️ <strong>کاشی تنظیمات سریع (QS Tile):</strong> شروع یا توقف فرآیند تونل‌سازی به صورت مستقیم از طریق مرکز کنترل اندروید.</li>
</ul>

<h2 style="text-align: right;">📸 تصاویر برنامه</h2>
<img width="250" height="550" alt="photo_2026-06-06_06-36-43" src="https://github.com/user-attachments/assets/1380c201-3955-409e-b9c8-97cd07b85d28" /><img width="250" height="550" alt="photo_2026-06-06_06-36-41" src="https://github.com/user-attachments/assets/c631db52-cf09-4a23-9c00-daed715054bd" /><img width="250" height="550" alt="photo_2026-06-06_06-36-42" src="https://github.com/user-attachments/assets/c0265cf3-c22f-4946-9e59-ada90973e5fd" />




<h2 style="text-align: right;">📥 چرخه انتشار و نصب</h2>
<p style="text-align: right;">ما از بخش Releases گیت‌هاب برای توزیع فایل‌های باینری برنامه خود استفاده می‌کنیم:</p>
<ul style="text-align: right; direction: rtl;">
  <li><strong>نسخه‌های قدیمی (پیش از <code>v0.9.0</code>):</strong> این بیلدها فاقد بهینه‌سازی‌های بنیادی مدیریت نخ (Threading) هستند و ممکن است تحت بار همزمانی سنگین دچار اشباع استخر اتصالات شوند. استفاده از آن‌ها به شدت منع می‌شود.</li>
  <li><strong>نسخه‌های پایدار و بهینه (<code>v0.9.0+</code>):</strong> فایل‌های APK کاملاً تأیید شده و آماده استفاده در محیط پروداکشن، همراه با همزمانی شبکه سطح پایین بازنویسی‌شده و کاهش چشمگیر مصرف انرژی.</li>
</ul>
<p style="text-align: right;">
به بخش <strong>Releases</strong> بروید، آخرین فایل <code>.apk</code> را دانلود و روی دستگاه اندرویدی خود نصب کنید.
</p>

<h2 style="text-align: right;">🛠 معماری فنی</h2>
<p style="text-align: right;">
حلقه اصلی شبکه برای عبور از محدودیت‌های استاندارد کوروتین‌های اندروید به شدت بازمهندسی شده است. به جای استفاده از دیسپچرهای پیش‌فرض کوروتین (مانند <code>Dispatchers.IO</code> که اجرای برنامه‌ها را به ۶۴ نخ موازی محدود می‌کند)، هسته شبکه از یک استخر نخ کش‌شده ناهمگام مبتنی بر جاوا (Cached Thread Pool) از طریق <code>Executors</code> بهره می‌برد. این کار تخصیص پویای نخ را برای هر سوکت پذیرفته‌شده تضمین می‌کند و پدیده گرسنگی نخ (Thread Starvation) و قفل شدن اپلیکیشن در هنگام بارگذاری سریع و متوالی محتوا (مانند استریم یا دانلود) را کاملاً از بین می‌برد.
</p>
<p style="text-align: right;"><strong>کتابخانه‌ها و فناوری‌های مورد استفاده:</strong></p>
<ul style="text-align: right; direction: rtl;">
  <li><strong>Kotlin (Coroutines & Executors):</strong> پیاده‌سازی هسته بک‌اند با ترکیب کوروتین‌های استاندارد با اکزکیوتورهای داینامیک سطح پایین جاوا برای پردازش ایمن و موازی سوکت‌ها.</li>
  <li><strong>Jetpack Compose:</strong> ابزار برجسته اندروید برای ساخت یک چیدمان فرانت‌اند تعاملی و واکنش‌گرا.</li>
  <li><strong>Android Native Services:</strong> بهره‌گیری از Foreground Services و اعلان‌ها برای حفظ تداوم فعالیت سوکت‌ها و رله‌های شبکه بدون بسته شدن تهاجمی توسط سیستم.</li>
  <li><strong>TileService:</strong> ادغام عمیق با زیرساخت اندروید جهت ارائه یک ابزار میان‌بر کاربردی در منوی تنظیمات سریع (QS).</li>
  <li><strong>Custom Sockets:</strong> پیاده‌سازی سوکت شبکه سطح پایین جاوا برای پردازش استریم‌های TCP و اجرای منطق سفارشی‌سازی شده فرگمنتیشن در مسیر ارسال.</li>
</ul>

<h2 style="text-align: right;">🚀 نصب و راه‌اندازی (Development)</h2>
<p style="text-align: right;">برای بیلد و تست کدها به صورت محلی، مراحل استاندارد Gradle را اجرا کنید:</p>

```bash
# ۱. کلون کردن مخزن
git clone https://github.com/mr-mingo/SNI-Spoofing-Android.git

# ۲. ورود به دایرکتوری
cd SNI-Spoofing-Android

# ۳. اسمبل کردن پروژه 
# اطمینان حاصل کنید که جاوا ۱۱ یا جدیدتر در محیط شما نصب باشد
./gradlew assembleDebug
```
<p style="text-align: right;">
شما می‌توانید فایل آرتیفکت تولید شده را مستقیماً از مسیر <code>app/build/outputs/apk/debug/</code> روی دستگاه هدفِ متصل شده خود نصب کنید.
</p>

<h2 style="text-align: right;">💡 نحوه کارکرد</h2>
<ol style="text-align: right; direction: rtl;">
  <li>برنامه را اجرا کرده و روی <strong>Start</strong> ضربه بزنید.</li>
  <li>یک پروکسی واسط محلی روی <code>127.0.0.1</code> (پورت پیش‌فرض <code>40443</code>) گوش خواهد داد.</li>
  <li>در داخل کلاینت VPN اصلی خود (مانند Hiddify یا v2rayNG)، مسیر خروجی (outbound route) یا پروکسی سرور مستقیم را طوری تنظیم کنید که به <code>127.0.0.1</code> در پورت تعیین شده اشاره کند.</li>
  <li>جریان اتصالات شما به طور کامل از طریق این اپلیکیشن به بیرون هدایت می‌شود، جایی که دست‌تکانی‌های TLS پیش از مسیریابی خارجی به سمت سرورهای هدف واقعی، عمداً خرد می‌شوند تا از فایروال‌های تحلیل عمیق بسته‌ها (DPI) در سطح ISP عبور کنند.</li>
</ol>

<h2 style="text-align: right;">🗺 نقشه راه (Roadmap)</h2>
<ul style="text-align: right; direction: rtl; list-style-type: none;">
  <li>[x] <code>v0.1.0 - v0.4.0</code>: معماری اصلی کاتلین، ادغام سرویس پروکسی محلی و بک‌اند پایه فرگمنتیشن.</li>
  <li>[x] <code>v0.5.0 - v0.8.0</code>: داشبورد زنده اتصالات، بهبود رابط کاربری Material 3 از طریق Jetpack Compose و پردازش قدرتمند لاگ‌ها.</li>
  <li>[x] <code>v0.9.x</code>: ادغام کاشی تنظیمات سریع (QS Tile)، بهینه‌سازی‌های باتری و آیکون هویت بصری سفارشی برند.</li>
  <li>[ ] <code>v1.0.0 (Stable)</code>: اولین استقرار پایدار رسمی که در بخش Releases در دسترس قرار می‌گیرد.</li>
</ul>

<h2 style="text-align: right;">📚 منابع (References)</h2>
<p style="text-align: right;">
منطق اصلی دور زدن محدودیت‌ها به شدت الهام گرفته از و به لحاظ ساختاری برگرفته از: <a href="https://github.com/patterniha/SNI-Spoofing">patterniha/SNI-Spoofing</a> است - تشکر صمیمانه از توسعه‌دهنده اصلی برای کار شگفت‌انگیزشان!
</p>

<h2 style="text-align: right;">⚖️ لایسنس</h2>
<p style="text-align: right;">
این پروژه تحت <strong>MIT License</strong> منتشر شده است. این پروژه کاملاً متن‌باز بوده و برای اعمال تغییرات آزادانه در دسترس است.
</p>

<h2 style="text-align: right;">☕ حمایت مالی (Donation)</h2>
<p style="text-align: right;">
اگر این پروژه را مفید یافتید و مایلید از توسعه آن حمایت کنید، می‌توانید از طریق یکی از شبکه‌های کریپتوی زیر کمک مالی خود را انجام دهید (با کلیک روی آیکون کنار کد، آدرس کپی می‌شود):
</p>

<div dir="ltr" style="text-align: left;">

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

</div>

<p style="text-align: right;">از حمایت شما متشکریم! 🙏</p>

</div>

---

## 🌟 Introduction
**SNI-Spoofing-Android** is a native, open-source Android application that creates a local proxy tunnel to help you bypass Deep Packet Inspection (DPI) and SNI-based filtering. It uses advanced TCP fragmentation techniques to spoof the Server Name Indication during the TLS handshake. The application operates entirely without root permissions and perfectly pairs as a routing overlay for clients like Hiddify or v2rayNG.

## ✨ Key Features
* 🛡️ **DPI Evasion:** Overcome SNI-based filtering limitations using robust TCP fragmentation mechanisms.
* 🧵 **High-Performance Multi-threading:** Powered by a customized cached thread pool architecture, avoiding core socket freezes and handling mass parallel connection overhead efficiently.
* 🔋 **Advanced Battery Management:** High energy efficiency through rapid lifecycle tracking and immediate termination of stale or dangling sockets.
* ⚡ **Local Proxy (Loopback):** Operates on `127.0.0.1` and runs non-intrusively without touching system VpnService, ensuring it doesn't conflict with other VPN tunnels.
* 🎨 **Modern Interface:** Experience a beautifully crafted, minimal, and user-friendly UI built natively using Jetpack Compose (Material 3).
* 📉 **Real-time Monitoring:** Keep track of live traffic streams, transmitted bytes, and active connection flows through the interactive dashboard.
* 🎛️ **Quick Settings (QS) Tile:** Start or stop the tunneling process directly from the Android control center.

## 📸 Screenshots
<img width="250" height="550" alt="photo_2026-06-06_06-36-43" src="https://github.com/user-attachments/assets/1380c201-3955-409e-b9c8-97cd07b85d28" /><img width="250" height="550" alt="photo_2026-06-06_06-36-41" src="https://github.com/user-attachments/assets/c631db52-cf09-4a23-9c00-daed715054bd" /><img width="250" height="550" alt="photo_2026-06-06_06-36-42" src="https://github.com/user-attachments/assets/c0265cf3-c22f-4946-9e59-ada90973e5fd" />

## 📥 Release Cycle & Installation
We utilize GitHub Releases to distribute our application binaries:
* **Old Versions (prior to `v0.9.0`):** These builds lack fundamental threading optimizations and can suffer from connection pool exhaustion under heavy concurrency loads. Their usage is highly discouraged.
* **Stable & Optimized Releases (`v0.9.0+`):** Production-ready, fully verified stable APKs with refined low-level network concurrency and highly reduced power consumption.

Go to the **Releases** section, download the latest `.apk` file, and install it on your Android device.

## 🛠 Technical Architecture
The core network loop has been heavily re-engineered to bypass standard Android coroutine limitations. Instead of using default coroutine dispatchers (such as `Dispatchers.IO`, which caps execution at 64 parallel threads), the networking core leverages an asynchronous Java-backed Cached Thread Pool via `Executors`. This ensures dynamic thread allocation for every accepted socket, completely eliminating thread starvation and app freezes during rapid sequential content loading (e.g., streaming or downloading).

**Libraries & Technologies used:**
* **Kotlin (Coroutines & Executors):** Core backend implementation combining standard coroutines with dynamic Java low-level executors for thread-safe concurrent socket parsing.
* **Jetpack Compose:** The prominent Android UI toolkit for constructing an interactive, reactive frontend layout.
* **Android Native Services:** Leveraging Foreground Services and Notifications to maintain sustained socket activity and network relays without aggressive system kills.
* **TileService:** Deeply integrated with the Android infrastructure to provide a functional QS Menu shortcut tool.
* **Custom Sockets:** Lower-level Java networking Socket implementation to process TCP streams and execute customized fragmentation logic in transit.

## 🚀 Installation & Setup
To build and test the codebase locally, run the standard Gradle procedures:

```bash
# 1. Clone the repository
git clone https://github.com/mr-mingo/SNI-Spoofing-Android.git

# 2. Change directory
cd SNI-Spoofing-Android

# 3. Assemble the project 
# Ensure Java 11+ or newer is installed in your environment
./gradlew assembleDebug
```
You can install the generated artifact directly from the `app/build/outputs/apk/debug/` directory to your connected target device.

## 💡 How it Works
1. Launch the app and tap **Start**.
2. A local intermediate proxy will listen on `127.0.0.1` (default port `40443`).
3. Inside your primary VPN client (like Hiddify or v2rayNG), configure the outbound route or direct server proxy target to point toward `127.0.0.1` at the designated port.
4. Your connection flows outbound entirely through this application, where TLS handshakes are fragmented intentionally to bypass ISP-level Deep Packet Inspection firewalls before routing externally to the actual target servers.

## 🗺 Roadmap
* [x] `v0.1.0 - v0.4.0`: Core Kotlin architecture, local proxy service integration, and basic fragmentation backend.
* [x] `v0.5.0 - v0.8.0`: Live connection dashboard, Material 3 UI enhancement via Jetpack Compose, and robust log parsing.
* [x] `v0.9.x`: Integrated Quick Settings Tile, battery optimizations, and custom brand identity icon.
* [ ] `v1.0.0 (Stable)`: Official first stable deployment made available under the Releases block.

## 📚 References
Core bypass logic is highly inspired by and structurally sourced from: [patterniha/SNI-Spoofing](https://github.com/patterniha/SNI-Spoofing) - Heartfelt thanks to the original developer for their amazing work!

## ⚖️ License
This project is released under the **MIT License**. It is fully open-source and freely available for modifications.

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
