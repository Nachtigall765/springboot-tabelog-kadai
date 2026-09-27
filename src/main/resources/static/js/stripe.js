const stripe = Stripe(pk_test_51U1SzSEo482CldTS4grjXehfTnGxlEZgHRB88NzFpLDdlGzS7WtdkCHR3NYbkb5p0iKuV65INDc0I2fAtrTDxkHQ00Zy6Kc9US);
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});