# SNI-Spoofing-Android 🚀

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android Platform](https://img.shields.io/badge/Platform-Android-green.svg?logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple.svg?logo=kotlin)](https://kotlinlang.org)

یک پیاده‌سازی پیشرفته و بومی اندروید از تکنیک دور زدن فیلترینگ و بازرسی عمیق بسته‌ها (DPI) به روش **SNI-Spoofing** که کاملاً با زبان **کاتلین (Kotlin)** توسعه یافته است.

این اپلیکیشن با ایجاد یک اینترفیس پروکسی/وی‌پی‌ان محلی روی دستگاه اندرویدی شما، بسته‌های ساختگی TLS ClientHello را با یک SNI جعلی (Spoofed) در طول دست‌اندازی TCP (Handshake) تزریق می‌کند. این کار باعث می‌شود تجهیزات DPI شبکه، یک دامنه مجاز (Decoy) را مشاهده کنند، در حالی که ترافیک واقعی شما به سلامت به مقصد اصلی خود هدایت می‌شود.

> 💡 **بر اساس ایده و منطق پروژه اصلی:** [patterniha/SNI-Spoofing](https://github.com/patterniha/SNI-Spoofing)

---

## ✨ ویژگی‌ها

* **هسته بومی اندروید:** توسعه‌یافته به صورت کامل با کاتلین جهت دریافت بالاترین کارایی و بهینه‌ترین مصرف باتری.
* **بای‌پاس حرفه‌ای DPI:** امکان تزریق بسته‌های ساختگی ClientHello با یک SNI کاملاً سفارشی و مجاز.
* **جعل اثر انگشت تی‌ال‌اس (uTLS):** شبیه‌سازی اثر انگشت مرورگرهای مدرن برای جلوگیری از مسدودسازی بر اساس فینگرپرینت:
  * Chrome, Firefox, Safari, Edge, iOS و غیره.
* **کنترل‌های پیشرفته تزریق (Injection):**
  * پشتیبانی از حالت‌های تزریق فعال و غیرفعال (Active/Passive).
  * قابلیت تکه‌تکه کردن بسته‌ها (Fragmentation) جهت خرد کردن ClientHello واقعی به بخش‌های سفارشی.
  * امکان تنظیم میزان تاخیر (Delay) و تعداد تکرار بسته‌های ساختگی.
* **ماتریس تست شبکه داخلی:** اجرای پیش‌تست (Preflight) درون اپلیکیشن برای بررسی خودکار اینکه کدام ترکیب از uTLS و SNI روی شبکه شما بهترین پاسخ را می‌دهد.
* **رابط کاربری مدرن Material 3:** طراحی شیک و تمیز همراه با پشتیبانی کامل از زبان‌های **فارسی** و انگلیسی.

---

## 📸 اسکرین‌شات‌ها

<p align="center">
  <img src="https://via.placeholder.com/250x500.png?text=Main+UI" width="250" alt="رابط کاربری اصلی" />
  <img src="https://via.placeholder.com/250x500.png?text=Settings+Screen" width="250" alt="تنظیمات" />
  <img src="https://via.placeholder.com/250x500.png?text=Test+Matrix" width="250" alt="ماتریس تست" />
</p>

---

## 📥 راهنمای نصب

ما از سیستم ریلیز گیتهاب برای انتشار برنامه‌ها استفاده می‌کنیم:

* **نسخه‌های آلفا/بتا (`v0.1.0` تا `v0.9.x`):** نسخه‌های آزمایشی و در حال توسعه هستند که تحت عنوان `Pre-release` منتشر می‌شوند. این نسخه‌ها شامل آخرین تغییرات هستند اما ممکن است باگ داشته باشند.
* **نسخه‌های پایدار (`v1.0.0+`):** نسخه‌های نهایی، کاملاً تست‌شده و بدون مشکل که برای استفاده عموم آماده هستند.

به بخش [**Releases**](https://github.com/mrdelinco/SNI-Spoofing-Android/releases) در همین ریپازیتوری بروید، آخرین فایل `.apk` سازگار با معماری پردازنده خود را دانلود و نصب کنید.

---

## 🛠 راهنمای استفاده سریع

1. **تنظیم مقصد:** آدرس IP یا هاست سرور بالادستی (Upstream) و پورت آن را وارد کنید (مثال: `104.19.229.21:443`).
2. **تنظیم SNI جعلی:** یک دامنه مجاز و سفید که در شبکه شما مسدود نیست وارد کنید (مثال: `hcaptcha.com`).
3. **انتخاب فینگرپرینت:** یک ساختار uTLS انتخاب کنید (مثال: `Firefox` یا `Chrome`).
4. **تست شبکه (پیشنهادی):** روی دکمه **Run Test Matrix** بزنید. اپلیکیشن ۲۴ حالت مختلف را بررسی کرده و بهترین کانفیگ را به شما نشان می‌دهد.
5. **اتصال:** سوئیچ **Start** را روشن کنید. در صورت نیاز، مجوز ایجاد VPN داخلی اندروید را تایید کنید.

### پارامترهای پیشرفته تنظیمات

| پارامتر | مقدار پیش‌فرض | توضیحات |
| :--- | :--- | :--- |
| **Fake SNI** | *ندارد* | دامنه ساختگی که سیستم فیلترینگ (DPI) آن را می‌بیند. |
| **uTLS Preset** | `Firefox` | اثر انگشت مرورگر برای بسته ClientHello. |
| **Injector Mode** | `Active` | حالت تزریق بسته‌ها به صورت فعال (`Active`) یا غیرفعال (`Passive`). |
| **Fake Repeat** | `1` | تعداد دفعات ارسال بلایندِ بسته‌های ساختگی. |
| **Fake Delay** | `2ms` | مدت زمان تاخیر قبل از فرستادن ترافیک اصلی دست‌اندازی. |
| **Enable Fragment**| `False` | تکه‌تکه کردن بسته‌های اصلی برای عبور از فیلترینگ سخت‌گیرانه. |
| **SNI Chunk Size** | `3 bytes` | سایز هر تکه در صورت فعال بودن قابلیت فِرگمنتیشن. |

---

## 🏗 نیازمندی‌ها و توسعه از روی سورس‌کد

### پیش‌نیازها
* Android Studio (نسخه Koala یا جدیدتر)
* Android SDK 34+
* Gradle 8.x+
* JDK 17+

### مراحل بیلد شخصی
1. مخزن را کلون کنید:
```bash
   git clone [https://github.com/mrdelinco/SNI-Spoofing-Android.git](https://github.com/mrdelinco/SNI-Spoofing-Android.git)
   cd SNI-Spoofing-Android

```

2. پروژه را در اندروید استودیو باز کنید.
3. بگذارید گریدل سینک (Sync) شود.
4. برای گرفتن خروجی نهایی APK، دستور زیر را در ترمینال اجرا کنید:

```bash
   ./gradlew assembleRelease

```

فایل نهایی در مسیر `app/build/outputs/apk/release/` ذخیره خواهد شد.

---

## 🗺 نقشه راه (Roadmap)

* [ ] **نسخه‌های v0.1.0 تا v0.4.0:** معماری اولیه کاتلین، پایداری سرویس VPN محلی و اینجکتور اکتیو پایه.
* [ ] **نسخه‌های v0.5.0 تا v0.8.0:** پیاده‌سازی حالت پاسیو، فرگمنتیشن بسته‌ها و تکمیل رابط کاربری Material 3.
* [ ] **نسخه‌های v0.9.x:** بهینه‌سازی کدهای شبکه و افزودن بخش ماتریس تست پیشرفته داخل اپ.
* [ ] **نسخه v1.0.0 (پایدار):** انتشار رسمی اولین نسخه استیبل همراه با کاشی تنظیمات سریع (Quick Settings Tile).

---

## 📄 لایسنس

این پروژه تحت لایسنس **MIT** منتشر شده است. برای اطلاعات بیشتر فایل [LICENSE](https://www.google.com/search?q=LICENSE) را مطالعه کنید.

---

## 🤝 تشکر و قدردانی

* از **[@patterniha](https://github.com/patterniha)** برای طراحی ایده، منطق و پایه پروژه اصلی [SNI-Spoofing](https://github.com/patterniha/SNI-Spoofing).

---

---

# English Version (Documentation)

An advanced Android implementation of the **SNI-Spoofing** DPI bypass technique, written natively in **Kotlin**.

This application creates a local VPN/Proxy interface on your Android device to inject fake TLS ClientHello packets with spoofed SNIs during the TCP handshake. This allows you to bypass Deep Packet Inspection (DPI) systems by showing them a decoy SNI, while your actual traffic safely reaches its intended destination.

> 💡 **Based on the original concept:** [patterniha/SNI-Spoofing](https://github.com/patterniha/SNI-Spoofing)

## ✨ Features

* **Native Android Core:** Built entirely with Kotlin for maximum performance.
* **DPI Bypass Backend:** Injects fake ClientHello packets with customizable decoy SNIs.
* **TLS Fingerprint Spoofing (uTLS):** Mimic modern browsers (Chrome, Firefox, Safari, Edge, etc.).
* **Built-in Network Test Matrix:** Run preflight checks inside the app to verify network compatibility.

## 📥 Installation

Go to the **[Releases](https://www.google.com/url?sa=E&source=gmail&q=https://github.com/mrdelinco/SNI-Spoofing-Android/releases)** section:

* **Pre-releases (`v0.1.0` to `v0.9.x`):** Testing versions containing experimental features.
* **Stable Releases (`v1.0.0+`):** Official stable production APKs.

## 🏗 Building from Source

```bash
git clone [https://github.com/mrdelinco/SNI-Spoofing-Android.git](https://github.com/mrdelinco/SNI-Spoofing-Android.git)
cd SNI-Spoofing-Android
./gradlew assembleRelease

```

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](https://www.google.com/search?q=LICENSE) file for details.

```

---

### 💡 یک کار دیگر که باید انجام دهید (ساخت فایل لایسنس):

برای اینکه گیتهاب به طور هوشمند پروژه شما را به عنوان لایسنس MIT بشناسد و بچِ (Badge) زرد رنگ آن فعال شود، باید یک فایل جداگانه به نام `LICENSE` (بدون هیچ پسوندی) در ریشه اصلی پروژه‌تان بسازید و متن زیر را داخل آن کپی کنید:

```text
MIT License

Copyright (c) 2026 mrdelinco

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
