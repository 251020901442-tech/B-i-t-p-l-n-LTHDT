document.addEventListener("DOMContentLoaded", function () {
  const cartButtons = document.querySelectorAll(".btn-add-cart");

  cartButtons.forEach((button) => {
    // Không chặn nút gửi Form của trang Đăng ký và Đăng nhập
    if (button.type === "submit" || button.closest("form")) return;

    button.addEventListener("click", function (e) {
      const card = this.closest(".book-card");
      const titleElement = card ? card.querySelector(".card-title") : null;
      if (!titleElement) return;

      e.preventDefault();
      const bookTitle = titleElement.innerText;

      const originalText = this.innerHTML;
      this.innerHTML = '<i class="fas fa-check"></i> Đã thêm';
      this.style.backgroundColor = "#28a745";

      setTimeout(() => {
        this.innerHTML = originalText;
        this.style.backgroundColor = "";
      }, 2000);

      console.log("Đã thêm vào giỏ hàng: " + bookTitle);
    });
  });
});
