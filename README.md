# 🚀 Scalable URL Shortener (Full-Stack)

This is a **high-performance URL Shortener** built with:
- **Backend:** Java 23, Spring Boot 3.4.2, Redis, PostgreSQL, AWS S3
- **Frontend:** Next.js 14, React, Tailwind CSS
- **Deployment:** Railway (Backend), Vercel (Frontend)

---

## 🔥 Features
✅ **Shorten URLs** → Instantly generate short URLs  
✅ **Expand URLs** → Redirect to the original URL  
✅ **Rate Limiting** → Prevent abuse with Redis-based rate limiting  
✅ **Caching** → Fast URL lookups with Redis  
✅ **Expiry-Based Cleanup** → Automatically deletes expired URLs  
✅ **AWS S3 Backups** → Expired URLs stored in S3 for auditing  
✅ **Minimal UI** → Clean and easy-to-use interface  
✅ **Copy & Toast Notifications** → Seamless user experience  

---

## 📦 Tech Stack
| Backend | Frontend | Infra/Deployment |
| ------- | -------- | ---------------- |
| Java 23 | Next.js 14 | Railway (Backend) |
| Spring Boot 3.4.2 | React.js | Vercel (Frontend) |
| PostgreSQL | Tailwind CSS | AWS (S3 for backups) |
| Redis | Axios | Docker (local) |

---

## 🚀 Quickstart (Local Development)

### 1️⃣ Backend (Spring Boot)
```bash
cd backend/
mvn clean install
mvn spring-boot:run
