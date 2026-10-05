# Build APK hanya dengan HP Android

Cara termudah tanpa PC adalah memakai GitHub Actions. HP hanya dipakai untuk mengunggah project dan mengambil APK hasil build; Android SDK berjalan di server GitHub.

## A. Buat repository GitHub
1. Buka github.com di Chrome.
2. Buat repository baru, misalnya `dsp-mixer-android`.
3. Upload seluruh isi folder project ini (bukan file ZIP-nya).
4. Pastikan folder `.github/workflows/android.yml` ikut ter-upload.

## B. Jalankan build
1. Buka tab **Actions** di repository.
2. Pilih workflow **Build Android APK**.
3. Tekan **Run workflow**.
4. Tunggu sampai status hijau.
5. Buka run tersebut → bagian **Artifacts** → download `dsp-mixer-debug-apk`.
6. Ekstrak ZIP artifact, lalu instal `app-debug.apk` di HP.

## C. Jika GitHub meminta izin
Pada repository pribadi, GitHub Actions tetap dapat dipakai. Jika Actions belum aktif, buka Settings → Actions → General dan izinkan workflow.

## Catatan
Project ini menargetkan Android modern dan membutuhkan izin mikrofon. Audio USB tetap bergantung pada dukungan perangkat Android/USB audio yang dipakai.
