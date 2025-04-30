<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-lg-4 col-md-6 col-sm-10">
        <h2 class="mb-4 text-center">Sign Up</h2>
        <ul class="alert alert-danger p-2 mb-4" v-if="backendErrors.length">
          <li v-for="(error, index) in backendErrors" :key="index">{{ error }}</li>
        </ul>
        <form @submit.prevent="handleRegister">
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input id="username" v-model="username" class="form-control" required/>
          </div>
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input id="email" type="email" v-model="email" class="form-control" required/>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input id="password" type="password" v-model="password" class="form-control" required/>
          </div>
          <div class="mb-3">
            <label for="firstName" class="form-label">First Name</label>
            <input id="firstName" v-model="firstName" class="form-control" />
          </div>
          <div class="mb-3">
            <label for="lastName" class="form-label">Last Name</label>
            <input id="lastName" v-model="lastName" class="form-control" />
          </div>
          <button :disabled="isSubmitting" type="submit" class="btn btn-primary w-100">Register</button>
          <p v-if="errorMessage" class="text-danger mt-2">{{ errorMessage }}</p>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { register } from '@/services/authService';
export default {
  data() {
    return {
      username: '',
      email: '',
      password: '',
      firstName: '',
      lastName: '',
      errorMessage: '',
      backendErrors: [],
      isSubmitting: false, // Prevent double submission: disable the button during loading if backend is slow
    };
  },
  methods: {
    async handleRegister() {
      this.backendErrors = [];
      this.errorMessage = '';
      this.isSubmitting = true;

      try {
        const user = {
          username: this.username,
          email: this.email,
          password: this.password,
          firstName: this.firstName,
          lastName: this.lastName
        };

        const result = await register(user);
        console.log('User created:', result);
        // Go to login page
        this.$router.push('/login');
      } catch (error) {
        const response = error.response?.data;
        if (response?.errors) {
          this.backendErrors = response.errors; // Backend returned a validation error list
        } else if (response?.message) {
          this.backendErrors = [response.message]; // ✅ wrap it in an array
        } else {
          this.errorMessage = "Something went wrong";
        }
      } finally {
        this.isSubmitting = false; // the button gets re-enabled even if an unexpected error occurs
      }
    }
  },
};
</script>
