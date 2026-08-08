# Virtu

**An Android ARM64 virtual machine app – run Windows, macOS, and Linux on your phone.**

[![Android](https://img.shields.io/badge/Android-8.0+-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen?style=flat-square)](https://developer.android.com)
[![ARM64](https://img.shields.io/badge/ARM64-v8a-blue?style=flat-square)](https://developer.android.com/ndk/guides/abis)
[![License](https://img.shields.io/badge/License-Custom-blue?style=flat-square)](LICENSE)
[![GitHub Stars](https://img.shields.io/github/stars/virtualsupport/virtu?style=flat-square)](https://github.com/virtualsupport/virtu/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/virtualsupport/virtu?style=flat-square)](https://github.com/virtualsupport/virtu/network)
[![GitHub Issues](https://img.shields.io/github/issues/virtualsupport/virtu?style=flat-square)](https://github.com/virtualsupport/virtu/issues)
[![GitHub Release](https://img.shields.io/github/release/virtualsupport/virtu?style=flat-square)](https://github.com/virtualsupport/virtu/releases)

---

## About

Virtu is a lightweight Android application that lets you run **Windows, macOS, and Linux** on your ARM64 device. No root access is required. It is designed to be accessible to everyone, from developers to casual users.

---

## Features

- **Linux distributions** – Run Linux via proot without root privileges.
- **Windows and macOS** – Emulate x86_64 operating systems using QEMU (user-provided ISO images).
- **Custom installer** – One‑click setup using curl scripts and a modular installation flow.
- **Touch controls** – Full VNC display with mouse, keyboard, and trackpad emulation.
- **Save and restore state** – Pause and resume your virtual machines at any time.
- **Audio support** – Sound output from the guest system.
- **Shared clipboard** – Copy and paste between Android and the VM.
- **Internet access** – NAT networking for the virtual machine.

---

## Requirements

- Android 8.0 (API 26) or higher.
- ARM64 device (most modern Android phones).
- 2 GB of RAM or more recommended for smooth operation.

---

## Quick Start

### Build from source

Clone the repository and build the APK using Gradle:

```bash
git clone https://github.com/virtualsupport/virtu.git
cd virtu
./gradlew assembleDebug
```

The resulting APK will be located at:

```
app/build/outputs/apk/debug/app-debug.apk
```

Install the APK

Copy the APK to your device and install it:

```bash
cp app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/virtu.apk
termux-open /sdcard/Download/virtu.apk
```

Or use ADB:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

Repository Structure

```
virtu/
├── app/                     # Android app module
│   ├── src/main/
│   │   ├── java/            # Kotlin source code
│   │   ├── res/             # Resources (layouts, strings, etc.)
│   │   ├── assets/          # Installer scripts and support files
│   │   ├── cpp/             # Native JNI code (C++)
│   │   └── jniLibs/         # Pre‑built native libraries (ARM64)
│   └── build.gradle.kts     # App‑level build configuration
├── .github/                 # GitHub Actions workflows and Dependabot
├── gradle/                  # Gradle wrapper files
├── build.gradle.kts         # Root build script
├── settings.gradle.kts      # Project settings
├── README.md
├── LICENSE
├── CONTRIBUTING.md
├── SECURITY.md
└── CHANGELOG.md
```

## Contributing

Contributions are welcome! Please read the Contributing Guide for details on how to report issues, suggest features, and submit pull requests.

---

## License

This project is licensed under the Custom License. See the LICENSE file for the full terms.

---

## Support

- Report bugs or request features via GitHub Issues.
- For general questions, feel free to open a discussion.

---

## Acknowledgments

Built with Kotlin, Compose, and the Android NDK. Powered by QEMU and proot.

---

### Made with care by the VirtuSupport team.
