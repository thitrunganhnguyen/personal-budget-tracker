<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-lg-4 col-md-6 col-sm-10">
        <h2 class="mb-4 text-center">Login</h2>
        <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
        <form @submit.prevent="handleLogin">
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input id="username" v-model="username" class="form-control" required/>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input id="password" type="password" v-model="password" class="form-control" required/>
          </div>
          <button :disabled="isSubmitting" type="submit" class="btn btn-primary w-100">Login</button>
        </form>
        <p class="text-center mt-3">
          Don't have an account?
          <RouterLink to="/register">Register here</RouterLink>
        </p>

      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/services/authService';
export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
      isSubmitting: false, // Prevent double submission: disable the button during loading if backend is slow
    };
  },
  methods: {
    async handleLogin() {
      this.errorMessage = '';
      this.isSubmitting = true;

      try {
        const result = await login(this.username, this.password);

        // Save JWT to localStorage
        localStorage.setItem('token', result.token);
        localStorage.setItem('user', JSON.stringify(result.user));

        // Redirect to dashboard
        this.$router.push('/dashboard');
      } catch (error) {
        const response = error.response?.data;
        if (response?.message) {
          this.errorMessage = response.message;
        } else {
          this.errorMessage = 'Login failed. Please try again.';
        }
      } finally {
        this.isSubmitting = false;
      }
    }
  },
};
</script>
