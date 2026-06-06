<div align="center">
  <img src="https://raw.githubusercontent.com/mr-mingo/SNI-Spoofing-Android/refs/heads/main/app/src/main/res/drawable/app_icon_sp_1780550720701.png" width="180" style="border-radius: 20px;" alt="SNI Spoofing Logo" />
  <h1>SNI-Spoofing-Android</h1>
  <p>🚀 یک کلاینت قدرتمند، سبک و متن‌باز برای دور زدن فیلترینگ و DPI از طریق جعل SNI و فرگمنتیشن در اندروید</p>
  <p>🚀 A powerful, lightweight, and open-source client to bypass DPI and filtering via SNI spoofing and fragmentation on Android</p>

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

<!-- ============================================== -->
<!-- ================= PERSIAN ==================== -->
<!-- ============================================== -->

<div dir="rtl" style="text-align: right;">

<h2 style="text-align: right;">🌟 معرفی پروژه</h2>
<p style="text-align: right;">
<strong>SNI-Spoofing-Android</strong> یک اپلیکیشن بومی و اوپن‌سورس اندرویدی است که با ایجاد یک تونل پروکسی محلی (Local Proxy) به شما در دور زدن سیستم‌های تحلیل عمیق بسته‌ها (DPI) و فیلترینگ مبتنی بر SNI کمک می‌کند. این برنامه از تکنیک‌های پیشرفته خرد کردن بسته‌های TCP (فرگمنتیشن) برای جعل نام سرور در طول دست‌تکانی TLS استفاده می‌کند. اپلیکیشن کاملاً بدون نیاز به دسترسی روت (Root) کار می‌کند و به عنوان یک لایه مسیریابی مکمل در کنار کلاینت‌هایی مانند Hiddify یا v2rayNG به طور بی‌نقص جفت می‌شود.
</p>

<h2 style="text-align: right;">✨ ویژگی‌های کلیدی</h2>
<ul style="text-align: right; direction: rtl;">
  <li>🛡️ <strong>دور زدن DPI:</strong> غلبه بر محدودیت‌های فیلترینگ مبتنی بر SNI با استفاده از مکانیسم‌های قدرتمند خرد کردن بسته‌های TCP (فرگمنتیشن).</li>
  <li>🧵 <strong>مالتی‌تردینگ با کارایی بالا:</strong> قدرت گرفته از معماری استخر نخ‌های کش‌شده سفارشی (Cached Thread Pool)، جهت جلوگیری از قفل شدن سوکت‌های اصلی و مدیریت کارآمد بار اتصالات موازی انبوه.</li>
  <li>🔋 <strong>مدیریت پیشرفته باتری:</strong> راندمان مصرف انرژی بالا از طریق ردیابی سریع چرخه حیات و قطع فوری سوکت‌های بلاتکلیف یا رها شده.</li>
  <li>⚡ <strong>پروکسی لوکال (Loopback):</strong> روی آدرس <code>127.0.0.1</code> عمل می‌کند و به صورت غیرتهاجمی بدون دستکاری VpnService سیستم اجرا می‌شود تا تضمین کند هیچ تداخلی با سایر تونل‌های VPN ایجاد نخواهد شد.</li>
  <li>🎨 <strong>رابط کاربری مدرن:</strong> تجربه یک طراحی بصری چشم‌نواز، مینیمال و کاربرپسند که به صورت بومی با استفاده از Jetpack Compose (Material 3) ساخته شده است.</li>
  <li>📉 <strong>مانیتورینگ زنده:</strong> ردیابی لحظه‌ای جریان ترافیک زنده، بایت‌های منتقل شده و جریان اتصالات فعال از طریق داشبورد تعاملی.</li>
  <li>🎛️ <strong>کاشی تنظیمات سریع (QS Tile):</strong> قطع و وصل مستقیم فرآیند تونل‌سازی از طریق مرکز کنترل (کنترل سنتر) اندروید.</li>
</ul>

<h2 style="text-align: right;">📥 روند انتشار نسخه‌ها و نصب</h2>
<p style="text-align: right;">ما همواره از سیستم انتشار بومی گیت‌هاب برای توزیع فایل‌های باینری برنامه استفاده کرده‌ایم:</p>
<ul style="text-align: right; direction: rtl;">
  <li><strong>نسخه‌های قدیمی (پیش از <code>v0.9.0</code>):</strong> این بیلدها فاقد بهینه‌سازی‌های بنیادی مدیریت نخ (Threading) بودند و به دلیل پدیده اشباع استخر اتصالات زیر بار سنگین، پایداری لازم را نداشتند. استفاده از آن‌ها به شدت منع می‌شود.</li>
  <li><strong>نسخه‌های پایدار و بهینه (<code>v0.9.0+</code>):</strong> فایل‌های APK کاملاً تأیید شده و آماده استفاده برای عموم، همراه با همزمانی شبکه سطح پایین بازنویسی‌شده و کاهش چشمگیر مصرف انرژی.</li>
</ul>
<p style="text-align: right;">
به بخش <a href="https://github.com/mr-mingo/SNI-Spoofing-Android/releases"><strong>Releases</strong></a> در همین مخزن بروید، آخرین فایل <code>.apk</code> را دانلود و روی دستگاه اندرویدی خود نصب کنید.
</p>

<h2 style="text-align: right;">🛠 ابعاد فنی و معماری</h2>
<p style="text-align: right;">
حلقه اصلی شبکه برای عبور از محدودیت‌های استاندارد کوروتین‌های اندروید به شدت بازمهندسی شده است. به جای استفاده از دیسپچرهای پیش‌فرض کوروتین (مانند <code>Dispatchers.IO</code> که اجرای برنامه‌ها را به سقف ۶۴ نخ موازی محدود می‌کند)، هسته شبکه از یک استخر نخ کش‌شده ناهمگام مبتنی بر جاوا (<code>Cached Thread Pool</code>) از طریق <code>Executors</code> بهره می‌برد. این کار تخصیص پویای نخ را برای هر سوکت پذیرفته‌شده تضمین می‌کند و پدیده گرسنگی نخ (Thread Starvation) و قفل شدن اپلیکیشن در هنگام بارگذاری سریع و متوالی محتوا (مانند استریم یا دانلود) را کاملاً از بین می‌برد.
</p>
<p style="text-align: right;"><strong>کتابخانه‌ها و فناوری‌های مورد استفاده:</strong></p>
<ul style="text-align: right; direction: rtl;">
  <li><strong>کاتلین (Coroutines & Executors):</strong> پیاده‌سازی هسته بک‌اند با ترکیب کوروتین‌های استاندارد و اکزکیوتورهای داینامیک سطح پایین جاوا برای پردازش ایمن و موازی سوکت‌ها.</li>
  <li><strong>جت‌پک کامپوز (Jetpack Compose):</strong> ابزار پیشرفته اندروید برای ساخت یک رابط کاربری تعاملی، واکنش‌گرا و مدرن در فرانت‌اند.</li>
  <li><strong>سرویس‌های بومی اندروید (Native Services):</strong> بهره‌گیری از Foreground Services و سیستم اعلان‌ها برای حفظ تداوم فعالیت سوکت‌ها و رله‌های شبکه بدون بسته شدن توسط مدیریت حافظه سیستم‌عامل.</li>
  <li><strong>سرویس کاشی (TileService):</strong> ادغام عمیق با زیرساخت اندروید جهت ارائه یک شورتکات کاربردی در منوی تنظیمات سریع (Quick Settings).</li>
  <li><strong>سوکت‌های سفارشی (Custom Sockets):</strong> پیاده‌سازی مستقیم سوکت‌های شبکه سطح پایین جاوا برای پردازش استریم‌های TCP و اعمال منطق فرگمنتیشن سفارشی در طول مسیر ارسال ترافیک.</li>
</ul>

<h2 style="text-align: right;">🚀 نصب و راه‌اندازی (محیط توسعه)</h2>
<p style="text-align: right;">برای بیلد و تست کدها به صورت محلی، مراحل استاندارد گریدل را اجرا کنید:</p>

```bash
# ۱. مخزن را کلون کنید
git clone https://github.com/mr-mingo/SNI-Spoofing-Android.git

# ۲. وارد پوشه پروژه شوید
cd SNI-Spoofing-Android

# ۳. پروژه را اسمبل کنید 
# اطمینان حاصل کنید که جاوا ۱۱ یا جدیدتر در محیط شما نصب باشد
./gradlew assembleDebug

0x9391475e4606322dc4a35daeef2da16910862afb

TSKr7K827wsDchiXiDhKS5J1bU1fN4AVF3

bc1q4jdhy3j2egfrm36pgv8f8senlesm5edvhuxuy9

DDDn4kCN9P3ThrHALrV8WnrYxfoMv6nrUK

UQCPjuSUe5OADe8ZkN_RDkEsmcff0jWgrmpKKoxfo8C0CVBN

# 1. Clone the repository
git clone https://github.com/mr-mingo/SNI-Spoofing-Android.git

# 2. Change directory
cd SNI-Spoofing-Android

# 3. Assemble the project 
# Ensure Java 11+ or newer is installed in your environment
./gradlew assembleDebug

0x9391475e4606322dc4a35daeef2da16910862afb

TSKr7K827wsDchiXiDhKS5J1bU1fN4AVF3

bc1q4jdhy3j2egfrm36pgv8f8senlesm5edvhuxuy9

DDDn4kCN9P3ThrHALrV8WnrYxfoMv6nrUK

UQCPjuSUe5OADe8ZkN_RDkEsmcff0jWgrmpKKoxfo8C0CVBN
