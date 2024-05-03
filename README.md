# HallowFall

## 專案概述

HallowFall-backend 為個人練習專案，自主進行全端網頁開發練習，實踐前後端分離架構、Docker 容器化管理及 Nginx 反向代理，精進 Vue.js、Git、JWT、MySQL 等技術及工具。

# 展示頁面
## [首頁展示DEMO](http://allenzone.ddns.net/)
## DNS 託管域名：allenzone.ddns.net
## [資料庫模型ER Model](http://allenzone.ddns.net/erm)
## [API文件Sweagger-ui](http://allenzone.ddns.net/swagger)

## 功能特色

### 前後端分離架構
使用前後端分離的架構，提高了應用程序的可擴展性和可維護性。
前端利用Vue.js框架創建動態且交互式的用戶界面，後端使用Java和Spring框架進行開發。
### API 安全保護
通過 DOMpurity 對用戶輸入的所有內容進行過濾，有效防禦XSS攻擊；後端接口對所有請求進行憑證檢查，防禦 HTTP 參數汙染攻擊。
### JWT 身份驗證
實現了JWT（JSON Web Tokens）作為核心身份驗證機制，提升安全性並無縫體驗。
JWT具有保留參數的功能，進一步降低請求次數，提升用戶體驗。
### MySQL 數據集成
實現了數據的存儲和檢索，用戶可以在動態且交互式的用戶界面中進行發文和留言等操作，同時保證了數據的完整性和安全性。
### Spring Security
後端使用了 Spring Security 框架實現了後端身份驗證，防禦 XSS 等惡意攻擊對後端的影響，確保了伺服器的安全。
同時可針對用戶進行權限管理，限制訪問請求。
### Vue Router 實現頁面導航
利用 Vue Router，實現了靈活和高效的頁面導航。用戶可以輕鬆地在不同視圖之間進行平滑過渡，增強了用戶的體驗感。
### 使用存儲過程提升安全性
在MySQL中實現了Stored Procedure，用於帳號創建和日誌記錄等功能。提升了應用程序的安全性，確保了數據的完整性和可靠性，同時抵禦SQL Injection。


## 環境設定

確保您的開發環境滿足以下要求：

- Node.js v20.12.0
- MySQL
- Java 17

## 安裝與設定
1. 切換到HallowFall-frontend目錄：
    ```
    cd HallowFall-frontend/
    ```
2. 安裝相關依賴：
    ```
    npm install
    ```
3. 導入MySQL數據庫結構：
    在MySQL中執行初始化文件：`\initialize_database.sql`

4. 設定SQL登入方式
    設定文件：`\HallowFall\backend\src\main\resources\application.properties`中的登入方式
    ```
    spring.datasource.username={your_username}
    spring.datasource.password={your_password}
    ```
5. 啟動後端服務
    - 運行 `\backend\src\main\java\tw\liangze\backend\BackendApplication.java`
    - 或是使用 Maven package 打包成.jar
    ```
    java -jar backend/target/backend-x.x.x-SNAPSHOT.jar
    ```
6. 啟動前端服務
    ```
    cd frontend
    npm run dev
    ```
7. 訪問 `http://localhost:5173/`
