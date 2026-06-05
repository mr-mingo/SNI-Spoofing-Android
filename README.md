<div align="center">
  <img src="https://img.icons8.com/color/144/shield.png" alt="SNI Spoofing Logo" />
  <h1>SNI-Spoofing-Android</h1>
  <p>🚀 یک کلاینت قدرتمند، سبک و متنباز برای دور زدن فیلترینگ و DPI از طریق جعل SNI و فرگمنتیشن در اندروید</p>

  <p>
    <a href="https://github.com/mr-mingo/SNI-Spoofing-Android/releases"><img src="https://img.shields.io/github/v/release/mr-mingo/SNI-Spoofing-Android?color=38BDF8&label=نسخه&style=for-the-badge" alt="Release"></a>
    <a href="https://github.com/mr-mingo/SNI-Spoofing-Android/blob/main/LICENSE"><img src="https://img.shields.io/github/license/mr-mingo/SNI-Spoofing-Android?color=10B981&label=لایسنس&style=for-the-badge" alt="License"></a>
    <a href="https://github.com/mr-mingo/SNI-Spoofing-Android/stargazers"><img src="https://img.shields.io/github/stars/mr-mingo/SNI-Spoofing-Android?color=EAB308&label=ستاره‌ها&logo=github&style=for-the-badge" alt="Stars"></a>
    <img src="https://img.shields.io/badge/پلتفرم-Android_7.0+-3DDC84?style=for-the-badge&logo=android" alt="Platform">
  </p>
</div>

---

<div dir="rtl">

## 🌟 معرفی پروژه
**SNI-Spoofing-Android** یک اپلیکیشن بومی و اوپن‌سورس اندرویدی است که با ایجاد یک تونل پروکسی محلی (Local Proxy) و استفاده از تکنیک‌های پیشرفته خرد کردن بسته‌ها (TCP Fragmentation) و جعل نام سرور (SNI Spoofing)، به شما اجازه می‌دهد از سد سیستم‌های تحلیل‌گر عمیق بسته‌ها (DPI) عبور کنید.
این برنامه بدون نیاز به دسترسی روت (Root) کار می‌کند و ابزاری عالی برای استفاده در کنار کلاینت‌هایی مانند Hiddify یا v2rayNG محسوب می‌شود.

## ✨ ویژگی‌های کلیدی
- 🛡️ **عبور از DPI:** دور زدن محدودیت‌های فیلترینگ مبتنی بر SNI با مکانیسم‌های قطعه‌قطعه کردن (Fragmentation).
- ⚡ **پروکسی لوکال (Loopback):** بدون تداخل با VPN‌های سیستم کار می‌کند و به صورت یک پروکسی واسط در پورت مشخص روی آدرس لوکال (`127.0.0.1`) اجرا می‌شود.
- 🎨 **رابط کاربری مدرن:** طراحی چشم‌نواز، مینیمال و کاربرپسند توسعه یافته با Jetpack Compose.
- 📉 **مانیتورینگ زنده:** مشاهده وضعیت ترافیک مصرفی، اتصالات فعال و تاریخچه لاگ‌ها در لحظه (Real-time).
- 🎛️ **شورتکات تنظیمات سریع (QS Tile):** امکان خاموش/روشن کردن پروکسی از طریق کنترل سنتر اندروید (Quick Settings).
- 🌓 **پشتیبانی از تم تاریک و روشن** + پشتیبانی از زبان فارسی و انگلیسی.

## 🛠 ابعاد فنی و معماری
این برنامه با استفاده از معماری مدرن اندروید و بهترین الگوهای توسعه طراحی شده است تا کمترین میزان مصرف باتری و بالاترین پرفورمنس را روی گوشی‌های موبایل داشته باشد. معماری برنامه به صورت Event-driven بوده و ارتباط بین تونل پروکسی و لایه UI از طریق استریم‌های پایدار جریان داده در کاتلین صورت می‌گیرد.

**کتابخانه‌ها و فریم‌ورک‌های مورد استفاده:**
* **Kotlin:** زبان برنامه‌نویسی اصلی با استفاده از Coroutines و StateFlow برای مدیریت Thread‌ها و عملیات‌های Asynchronous.
* **Jetpack Compose:** فریم‌ورک اصلی برای ساخت رابط کاربری (UI) مدرن به جای XML.
* **Android Service & Foreground Notifications:** برای زنده نگه‌داشتن پروسس‌ها در پس‌زمینه بدون کشته شدن توسط سیستم‌عامل.
* **TileService API:** برای هندل کردن دکمه منوی تنظیمات سریع (Quick Setting).
* **Network Sockets:** پیاده‌سازی سوکت‌های سطح پایین پروتکل TCP برای رله کردن ریکوئست‌ها و ایجاد Fragmentation در سطح کلاینت.

## 🚀 نصب و راه‌اندازی (Development)

برای اجرای سورس کد روی ماشین توسعه خودت، مراحل زیر را طی کن:

```bash
# 1. مخزن را کلون کنید
git clone https://github.com/mr-mingo/SNI-Spoofing-Android.git

# 2. وارد پوشه پروژه شوید
cd SNI-Spoofing-Android

# 3. با استفاده از گریدل پروژه را بیلد کنید
# اطمینان حاصل کنید که جاوا 11+ روی سیستم نصب باشد
./gradlew assembleDebug
```
شما می‌توانید فایل APK خروجی را مستقیماً از پوشه `app/build/outputs/apk/debug/` روی گوشی نصب کنید.

## 💡 چگونه کار می‌کند؟
1. اپلیکیشن را اجرا کرده و دکمه **شروع (Start)** را بزنید. 
2. اپلیکیشن یک پروکسی روی `127.0.0.1` (به صورت پیش‌فرض با پورت `40443`) اجرا می‌کند.
3. در کلاینت مورد نظر خود (مثل Hiddify یا v2rayNG)، در پنل کانفیگ، آدرس اتصال یا Routing را به سرور `127.0.0.1` و پورت تنظیم شده تغییر دهید.
4. تمام ریکوئست‌های ارسالی کلاینت شما، قبل از خروج به فضای اینترنت، از این اپلیکیشن رد می‌شوند، پردازش و Fragment می‌شوند و در نهایت به سمت سرور واقعی (شما) پرتاب می‌شوند.

## 📚 مراجع و رفرنس‌ها
* Core Bypass Logic based on: [patterniha/SNI-Spoofing](https://github.com/patterniha/SNI-Spoofing)

## ⚖️ لایسنس
این پروژه تحت پروانه متن‌باز **MIT License** منتشر شده است. استفاده و تغییر در سورس کد آزاد می‌باشد.

## ☕ حمایت مالی

اگر این پروژه برایتان مفید بود و تمایل داشتید از توسعه آن حمایت کنید، می‌توانید از طریق شبکه‌های رمزارز زیر دونیت کنید:

آدرس شبکه‌های ETH, BNB, MATIC (ERC20, BEP20):
```text
0x9391475e4606322dc4a35daeef2da16910862afb
```

آدرس شبکه TRON (TRC20):
```text
TSKr7K827wsDchiXiDhKS5J1bU1fN4AVF3
```

آدرس شبکه Bitcoin:
```text
bc1q4jdhy3j2egfrm36pgv8f8senlesm5edvhuxuy9
```

آدرس شبکه Dogecoin:
```text
DDDn4kCN9P3ThrHALrV8WnrYxfoMv6nrUK
```

آدرس شبکه TON:
```text
UQCPjuSUe5OADe8ZkN_RDkEsmcff0jWgrmpKKoxfo8C0CVBN
```
از حمایت شما متشکرم! 🙏

</div>

---

<div dir="ltr">

## 🌟 Introduction
**SNI-Spoofing-Android** is a native, open-source Android application that creates a local proxy tunnel to help you bypass Deep Packet Inspection (DPI) and SNI-based filtering. It uses advanced TCP fragmentation techniques to spoof the Server Name Indication during the TLS handshake.
The application operates entirely without root permissions and perfectly pairs as a routing overlay for clients like Hiddify or v2rayNG.

## ✨ Key Features
- 🛡️ **DPI Evasion:** Overcome SNI-based filtering limitations using robust TCP fragmentation mechanisms.
- ⚡ **Local Proxy (Loopback):** Operates on `127.0.0.1` and runs non-intrusively without touching system VpnService, ensuring it doesn't conflict with other VPN tunnels.
- 🎨 **Modern Interface:** Experience a beautifully crafted, minimal, and user-friendly UI built natively using Jetpack Compose.
- 📉 **Real-time Monitoring:** Keep track of live traffic streams, transmitted bytes, and active connection flows through the interactive dashboard.
- 🎛️ **Quick Settings (QS) Tile:** Start or stop the tunneling process directly from the Android control center.
- 🌓 **Dynamic Theming:** Seamless support for Dark & Light modes, adapting directly to your system preferences. Fully Bilingual (English & Persian).

## 🛠 Technical Architecture
The app is engineered using modern Android development patterns, designed to offer high performance, minimal battery consumption, and maximum reliability on mobile platforms. Interaction between the background proxy engine and the UI relies on stable, reactive stream flows in Kotlin.

**Libraries & Technologies used:**
* **Kotlin:** Core backend implementation, taking advantage of Coroutines and `StateFlow` for state management and async socket processing.
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
# Ensure Java 11+ is installed in your environment
./gradlew assembleDebug
```
You can install the generated artifact directly from the `app/build/outputs/apk/debug/` directory to your connected target device.

## 💡 How it Works
1. Launch the app and tap **Start**.
2. A local intermediate proxy will listen on `127.0.0.1` (default port `40443`).
3. Inside your primary VPN client (like Hiddify or v2rayNG), configure the outbound route or direct server proxy target to point toward `127.0.0.1` at the designated port.
4. Your connection flows outbound entirely through this application, where TLS handshakes are fragmented intentionally to bypass ISP-level Deep Packet Inspection firewalls before routing externally to the actual target servers.

## 📚 References
* Core bypass logic is highly inspired by and structurally sourced from: [patterniha/SNI-Spoofing](https://github.com/patterniha/SNI-Spoofing)

## ⚖️ License
This project is released under the **MIT License**. It is fully open-source and freely available for modifications.

## ☕ Donation

If you found this project useful and would like to support its development, you can make a donation in one of the following crypto networks:

ETH, BNB, MATIC network (ERC20, BEP20):
```text
0x9391475e4606322dc4a35daeef2da16910862afb
```

TRON network (TRC20):
```text
TSKr7K827wsDchiXiDhKS5J1bU1fN4AVF3
```

Bitcoin network:
```text
bc1q4jdhy3j2egfrm36pgv8f8senlesm5edvhuxuy9
```

Dogecoin network:
```text
DDDn4kCN9P3ThrHALrV8WnrYxfoMv6nrUK
```

TON network:
```text
UQCPjuSUe5OADe8ZkN_RDkEsmcff0jWgrmpKKoxfo8C0CVBN
```
Thank you for your support! 🙏

</div>
