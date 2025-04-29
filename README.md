# mono

このリポジトリは、ユーザー管理のためのモノリポジトリ（フロントエンド + バックエンド）です。

- **バックエンド**: Quarkus + Flyway + PostgreSQL（Docker利用）
- **フロントエンド**: SvelteKit
- **DB**: PostgreSQL（ユーザーテーブル）

---

## 必要な環境

- Node.js (v18以上推奨)
- Docker / Docker Compose
- Java 21
- Maven

---

## セットアップ手順

### 1. リポジトリのクローン

```bash
git clone https://github.com/kenji-kimura1995/mono
cd mono
```

### 2. データベース(PostgreSQL)の起動

```bash
cd mono-back
docker compose up -d
```

### 3. バックエンド(Quarkus)の起動

```bash
cd mono-back
mvn quarkus:dev
```

### 4. フロントエンド(SvelteKit)の起動

```bash
cd mono-front
npm install
npm run dev
```

### 5. アクセス

フロントエンド: http://localhost:5173
バックエンド: http://localhost:8080

### フォルダ構成

mono/
├── mono-back/   # バックエンドAPI (Quarkus)
├── mono-front/  # フロントエンドアプリ (SvelteKit)
└── README.md