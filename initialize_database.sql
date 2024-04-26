-- 資料庫初始化(!!!!!DROP 慎用!!!!!)
DROP DATABASE IF EXISTS HallowDB;
CREATE DATABASE HallowDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE HallowDB;


-- 創建使用者資料表
CREATE TABLE IF NOT EXISTS user (
    user_id INT AUTO_INCREMENT PRIMARY KEY, -- 使用者 ID (Primary Key)
    phone VARCHAR(100) UNIQUE NOT NULL, -- 電話(手機號碼)
    email VARCHAR(100) UNIQUE, -- 電子郵件
    password VARCHAR(255) NOT NULL,-- 密碼
    role VARCHAR(20) DEFAULT 'USER',-- 使用者權限
    deleted BOOLEAN DEFAULT FALSE, -- 刪除標記
    nickname VARCHAR(50) NOT NULL, -- 用者名稱，可重複(允許不同使用者擁有相同名子)
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, --建立時間
    avatar MEDIUMTEXT DEFAULT NULL, -- 頭像(Image)
    cover_image MEDIUMTEXT DEFAULT NULL, -- 封面照片
    signature VARCHAR(3000) DEFAULT '<p></p>' -- 使用者的自我介紹(Biography)
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
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 最後編輯時間
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
INSERT INTO user (phone,email,password,role,nickname,signature) VALUES
('root','root@root.com','$2a$10$.UAoeaAVeH8vhPsxHaw1I.teyo3iBunZllqraM1EmHQJwk1CkwD8u','ROOT','Root管理者','<h3>歡迎使用本服務！</h3>'),
('admin','admin@admin.com','$2a$10$.UAoeaAVeH8vhPsxHaw1I.teyo3iBunZllqraM1EmHQJwk1CkwD8u','ADMIN','王五','<h3>我不是張三。</h3>'),
('0912345678','user@user.com','$2a$10$.UAoeaAVeH8vhPsxHaw1I.teyo3iBunZllqraM1EmHQJwk1CkwD8u','USER','趙六','<h3>我不是李四。</h3>');

INSERT INTO post(user_id,title,content,image) VALUES
(1,'祝大家事事如意，闔家平安','<h3>為什麼祝大家事事如意</h3><p>闔家平安對我們來說這麼重要？我們都知道，只要有意義，那麽就必須慎重考慮。更多祝大家事事如意，闔家平安的意義是這樣的，黑格爾曾經說過一句發人深省的話，只有永遠躺在泥坑里的人，才不會再掉進坑里。帶著這句話，我們還要更加慎重的審視這個問題：我們一般認為，抓住了問題的關鍵，其他一切則會迎刃而解。</p>','https://i.imgur.com/QVbwPXD.png'),
(2,'塑膠袋','<h2>塑膠袋，神仙之物：</h2><br /><h2>望之透明如無物；</h2><br /><h2>觸之絲滑如綢緞。</h2>','https://i.imgur.com/oqGcpY7.jpg'),
(3,'玉山銀行','<h2>玉山銀行真的是很值得探究</h2><p>要想清楚，玉山銀行，到底是一種怎麼樣的存在。</p><br /><p>俾斯麥曾經告訴世人，對於不屈不撓的人來說，沒有失敗這回事。這讓我思索了許久，為什麼玉山銀行對我們來說這麼重要？</p><br /><p>一般來講，我們都必須務必慎重的考慮考慮。在這種困難的抉擇下，本人思來想去，寢食難安。玉山銀行的意義其實就隱藏在我們的生活中，了解清楚玉山銀行到底是一種怎麼樣的存在，是解決一切問題的關鍵。</p>','https://i.imgur.com/yQ6tq1I.jpeg');

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
  IN newPhone VARCHAR(100),
  IN newPassword VARCHAR(255),
  IN newNickname VARCHAR(50)
)
BEGIN
  INSERT INTO user (phone, password, nickname) VALUES (newPhone, newPassword, newNickname);
END;

-- 創建User_log Stored Procedure 用以紀錄log
CREATE PROCEDURE InsertUserLog (
  IN newPhone VARCHAR(100),
  IN newIP VARCHAR(50),
  IN newStatus VARCHAR(15)
)
BEGIN
  DECLARE userId INT;

  -- 根據 phone 查找對應的 user_id
  SELECT user_id INTO userId FROM user WHERE phone = newPhone;

  -- 如果找到對應的 user_id，則插入 user_log 記錄
  IF userId IS NOT NULL THEN
    INSERT INTO user_log (user_id, status, ip_address) VALUES (userId, newStatus, newIP);
  END IF;
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


