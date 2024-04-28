-- 資料庫初始化(!!!!!DROP 慎用!!!!!)
DROP DATABASE IF EXISTS HallowDB;
CREATE DATABASE HallowDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE HallowDB;

-- 後端登入身份初始化
CREATE USER 'watcher'@'%' IDENTIFIED BY 'P@ssw0rd';
GRANT ALL PRIVILEGES ON *.* TO 'watcher'@'%';

-- 創建使用者資料表
CREATE TABLE IF NOT EXISTS user (
    user_id INT AUTO_INCREMENT PRIMARY KEY, -- 使用者 ID (Primary Key)
    phone VARCHAR(100) UNIQUE, -- 電話(手機號碼)
    email VARCHAR(100) UNIQUE NOT NULL , -- 電子郵件
    password VARCHAR(255) NOT NULL,-- 密碼
    role VARCHAR(20) DEFAULT 'USER',-- 使用者權限
    deleted BOOLEAN DEFAULT FALSE, -- 刪除標記
    nickname VARCHAR(50) NOT NULL, -- 用者名稱，可重複(允許不同使用者擁有相同名子)
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, --建立時間
    avatar MEDIUMTEXT DEFAULT NULL, -- 頭像(Image)
    cover_image MEDIUMTEXT DEFAULT NULL, -- 封面照片
    signature VARCHAR(3000) DEFAULT '<p></p>', -- 使用者的自我介紹(Biography)
    CONSTRAINT email_readonly CHECK (email = email) -- 設定 email 為不可更改
);

-- 加入聯合索引(考慮到未來可能常用phone或email查找)
CREATE INDEX idx_phone_email ON user (phone, email);

-- 創建使用者紀錄資料表
CREATE TABLE IF NOT EXISTS user_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY, -- 流水號
    user_id INT, -- Foreign Key to User
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 操作時間
    status VARCHAR(15) NOT NULL, -- 操作內容
    ip_address VARCHAR(50) NOT NULL, -- 來源位置
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);
-- 加入索引
CREATE INDEX idx_user_id ON user_log (user_id);

-- 創建發文資料表
CREATE TABLE IF NOT EXISTS post(
  post_id INT AUTO_INCREMENT PRIMARY KEY, -- 發文流水號(Primary Key)
  user_id INT NOT NULL, -- Foreign Key to User
  title VARCHAR(255) NOT NULL, -- 文章標題
  content MEDIUMTEXT, -- 文章內容
  image MEDIUMTEXT DEFAULT NULL, -- 文章封面圖片
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 發文時間
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 最後編輯時間
  deleted BOOLEAN DEFAULT FALSE, -- 刪除標記 
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);
-- 加入user_id索引
CREATE INDEX idx_user_id ON post(user_id);

-- 創建留言資料表
CREATE TABLE IF NOT EXISTS comment(
  comment_id INT AUTO_INCREMENT PRIMARY KEY, -- 留言 ID (Primary Key)
  user_id INT NOT NULL, -- Foreign Key to user
  post_id INT NOT NULL, -- Foreign Key to post
  content VARCHAR(255) NOT NULL, -- 留言內容
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 留言時間
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (post_id) REFERENCES post(post_id)
);
-- 加入post_id索引
CREATE INDEX idx_post_id ON comment(post_id);

-- 初始化測試資料(密碼皆為經過 BCrypt 雜湊的 123456)
INSERT INTO user (phone,email,password,role,nickname,signature,avatar) VALUES
('root','root@root.com','$2a$10$.UAoeaAVeH8vhPsxHaw1I.teyo3iBunZllqraM1EmHQJwk1CkwD8u','ROOT','Root管理者','<h3>歡迎使用本服務！</h3>','https://cdn.pixabay.com/photo/2016/07/02/12/21/eclipse-1492818_1280.jpg'),
('admin','admin@admin.com','$2a$10$.UAoeaAVeH8vhPsxHaw1I.teyo3iBunZllqraM1EmHQJwk1CkwD8u','ADMIN','王五','<h3>我不是張三。</h3>','https://cdn.pixabay.com/photo/2022/05/23/07/03/woman-7215421_1280.jpg'),
('0912345678','user@user.com','$2a$10$.UAoeaAVeH8vhPsxHaw1I.teyo3iBunZllqraM1EmHQJwk1CkwD8u','USER','趙六','<h3>我不是李四。</h3>','https://cdn.pixabay.com/photo/2022/07/24/23/46/artificial-intelligence-7342613_1280.jpg');

INSERT INTO post(user_id,title,content,image) VALUES
(1,'伺服器 Nginx 反向代理','# Nginx反向代理在前後端分離項目中的優點<br /><br />
Nginx反向代理是一種在網絡架構中使用的技術，它通過將客戶端的請求轉發給後端服務器，然後將後端服務器的響應返回給客戶端，來實現對後端服務器的代理。相比於正向代理，反向代理是指用於伺服器端的代理。<br /><br />
以下是Nginx反向代理在前後端分離項目中的一些優點：<br /><br />
- **負載均衡**：Nginx可以作為反向代理在多個後端服務器間分發請求，從而實現負載均衡。這有助於提高系統的性能和可擴展性，因為多個服務器可以共同處理請求，減輕單個服務器的壓力。<br /><br />
- **安全性**：通過反向代理，可以隱藏後端服務器的實際IP地址和結構，從而增加系統的安全性。此外，Nginx具有強大的安全功能，如DDoS防護和SSL/TLS終端。<br /><br />
- **靜態資源緩存**：Nginx可以將靜態資源（如圖像、CSS和JavaScript文件）緩存到內存中，從而減少對後端服務器的請求量，提高網頁加載速度和性能。<br /><br />
- **反向代理與Web服務器分離**：通過使用Nginx作為反向代理，可以將Web服務器（如Tomcat、Node.js等）與公共網絡分離，增加系統的安全性和可靠性。<br /><br />
- **URL重寫**：Nginx可以通過URL重寫功能，對請求的URL進行修改和重寫，從而實現更靈活的URL管理和路由控制。<br /><br />
總的來說，Nginx反向代理在前後端分離項目中具有關鍵的作用，可以提高系統的性能、安全性和可靠性，同時還能夠實現負載均衡和靜態資源緩存等功能，是現代Web應用開發中常用的技術之一。
','https://i.imgur.com/QVbwPXD.png'),
(2,'塑膠袋','<h2>塑膠袋，神仙之物：</h2><br /><h2>望之透明如無物；</h2><br /><h2>觸之絲滑如綢緞。</h2>','https://ct.yimg.com/xd/api/res/1.2/sY0iEeW_3tnaBTMPndPPPw--/YXBwaWQ9eXR3YXVjdGlvbnNlcnZpY2U7aD0yNTYxO3E9ODU7cm90YXRlPWF1dG87dz0yNDQ5/https://s.yimg.com/ob/image/be934b13-3d96-4012-95b7-4c85877fa70a.jpg'),
(3,' Spring Security 與 JWT 認證','# Spring Security與JWT認證<br /><br />
在現代的Web應用開發中，安全性是至關重要的一環。Spring Security是一個強大且廣泛使用的安全框架，它提供了許多功能來保護應用程序免受各種攻擊。與此同時，JSON Web Token（JWT）作為一種輕量級且安全的身份驗證標準，被廣泛應用於跨平台的身份驗證和授權機制。<br /><br />
## Spring Security的基礎<br /><br />
Spring Security通過使用範圍、標記和過濾器來確保應用程序的安全性。它可以用於控制對URL的訪問權限、實現身份驗證和授權、管理用戶會話等。<br /><br />
## JWT認證的優點<br /><br />
JSON Web Token（JWT）是一種輕量級的、安全的身份驗證標準，具有以下優點：
- **簡單且自包含**：JWT是一個簡單的token，它包含了用戶的身份信息以及相關的元數據，可以自我描述並且易於傳輸。
- **無狀態性**：由於JWT包含了所有必要的信息，因此不需要在服務端保存用戶的會話狀態，可以實現無狀態的身份驗證。
- **安全性**：JWT使用簽名來保護token的完整性，可以防止篡改和偽造。<br /><br />
## Spring Security與JWT的集成<br /><br />
Spring Security可以與JWT集成，實現基於token的身份驗證和授權機制。通常，這包括以下步驟：
1. **生成JWT**：當用戶登錄成功時，服務端會生成一個JWT，包含用戶的身份信息和相關元數據，並將其返回給客戶端。
2. **驗證JWT**：當客戶端發送請求時，將JWT放入請求的頭部或參數中。服務端會驗證JWT的有效性，包括檢查簽名、過期時間等。
3. **授權檢查**：服務端可以根據JWT中包含的用戶信息進行授權檢查，確定用戶是否有權訪問特定的資源或執行特定的操作。<br /><br />
## 結語<br /><br />
Spring Security與JWT的集成提供了一個安全、無狀態的身份驗證機制，適用於各種現代Web應用開發場景。通過結合Spring Security的強大功能和JWT的輕量、安全特性，我們可以實現更安全、更可靠的身份驗證和授權機制，保護應用程序免受各種攻擊。<br /><br />
這段文章簡要介紹了Spring Security與JWT認證的相關內容，希望對你有所幫助！
','https://hashnode.com/utility/r?url=https:%2F%2Fcdn.hashnode.com%2Fres%2Fhashnode%2Fimage%2Fupload%2Fv1633101239445%2F1LPg4fxdV.png%3Fw%3D1200%26h%3D630%26fit%3Dcrop%26crop%3Dentropy%26auto%3Dcompress%2Cformat%26format%3Dwebp%26fm%3Dpng');

INSERT INTO comment(user_id,post_id,content) VALUES
  (1, 1, '這篇文章很有啟發性，值得好好思考'),
  (2, 1, '作者的觀點很深刻，讓我受益匪淺'),
  (3, 1, '非常喜歡這篇文章，期待更多相關內容'),
  (1, 2, '我對這個議題有自己獨特的看法，不完全同意作者的觀點'),
  (2, 2, '感謝作者分享這篇文章，增加了我的見識'),
  (3, 2, '作者提出的問題很值得討論，希望能有更多相似文章'),
  (1, 3, '這篇文章引起了我的共鳴，我有相似的經歷'),
  (2, 3, '看完這篇文章，讓我對生活有了新的理解'),
  (3, 3, '作者的文字寫得很生動，讓我能夠深刻感受到情境'),
  (1, 1, '這篇文章的結構很清晰，讓我容易理解其中的論點'),
  (2, 1, '我希望能夠看到作者更多不同主題的文章'),
  (3, 1, '這是我近期讀過最棒的一篇文章，感謝作者的分享'),
  (1, 2, '這個問題很有爭議性，我認為可以從多角度來探討'),
  (2, 2, '我覺得作者的論述很有說服力，值得一讀再讀'),
  (3, 2, '希望能看到作者更多深入的研究報告，加油！');

-- 創建User Stored Procedure 用以註冊
CREATE PROCEDURE InsertUser (
  IN newEmail VARCHAR(100),
  IN newPassword VARCHAR(255),
  IN newNickname VARCHAR(50)
)
BEGIN
  INSERT INTO user (email, password, nickname) VALUES (newEmail, newPassword, newNickname);
END;

-- 創建User_log Stored Procedure 用以紀錄log
CREATE PROCEDURE InsertUserLog (
  IN newUserId int,
  IN newIP VARCHAR(50),
  IN newStatus VARCHAR(15)
)
BEGIN
    INSERT INTO user_log (user_id, ip_address,status) VALUES (newUserId,newIP, newStatus);
END;


-- 創建Post Stored Procedure 用以發文
CREATE PROCEDURE InsertPost (
  IN inputUserId INT,
  IN newTitle VARCHAR(255),
  IN NewContent MEDIUMTEXT,
  IN NewImage MEDIUMTEXT
)
BEGIN
  INSERT INTO post(user_id,title,content,image) VALUES (inputUserId,newTitle,NewContent,NewImage);
END;

-- 創建Comment Stored Procedure 用以留言
CREATE PROCEDURE InsertComment (
  IN inputPostId INT,
  IN inputUserId INT,
  IN NewContent VARCHAR(255)
)
BEGIN
  INSERT INTO comment(post_id,user_id,content) VALUES (inputPostId,inputUserId,NewContent);
END;


