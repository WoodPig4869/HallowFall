<template>
  <div class="container mt-3">
    <h1 class="mb-4">HallowFall</h1>

    <div class="card">
      <div class="card-body">
        <h2 class="card-title">專案概述</h2>
        <p class="card-text">HallowFall-backend是一個包含會員功能和社群媒體（發文和留言）功能的網站後端專案。旨在提供一個安全、可擴展和可維護的平台，讓用戶可以進行註冊、登錄、發文和留言等操作。
        </p>
      </div>
    </div>

    <div class="card mt-4">
      <div class="card-body">
        <h2 class="card-title">功能特色</h2>
        <ul class="list-group list-group-flush">
          <li class="list-group-item">
            <strong>前後端分離：</strong>使用前後端分離的架構，提高了應用程序的可擴展性和可維護性。前端利用Vue.js框架創建動態且交互式的用戶界面，後端使用Java和Spring框架進行開發。</li>
          <li class="list-group-item"><strong>JWT 身份驗證：</strong>實現了JWT（JSON Web
            Tokens）作為核心身份驗證機制，提升安全性並無縫體驗。JWT具有保留參數的功能，進一步降低請求次數，提升用戶體驗。</li>
          <li class="list-group-item"><strong>MySQL
              數據集成：</strong>實現了數據的存儲和檢索，用戶可以在動態且交互式的用戶界面中進行發文和留言等操作，同時保證了數據的完整性和安全性。</li>
          <li class="list-group-item"><strong>Spring Security：</strong>後端使用了Spring
            Security框架實現了後端身份驗證，防禦XSS等惡意攻擊，確保了伺服器的安全。同時可針對用戶進行權限管理，限制訪問請求。</li>
          <li class="list-group-item"><strong>Vue Router 實現頁面導航：</strong>利用Vue
            Router，實現了靈活和高效的頁面導航。用戶可以輕鬆地在不同視圖之間進行平滑過渡，增強了用戶的體驗感。</li>
          <li class="list-group-item"><strong>使用存儲過程提升安全性：</strong>在MySQL中實現了Stored
            Procedure，用於帳號創建和日誌記錄等功能。提升了應用程序的安全性，確保了數據的完整性和可靠性，同時抵禦SQL Injection。</li>
        </ul>
      </div>
    </div>
  </div>

  <br />
  <br />
  <br />
  <br />
  <br />
</template>
<script>
</script>

<style>
.fs-5 {
  text-align: left;
}
</style>
