<!-- Categories.vue -->
<template>
  <div class="container mt-5">
    <h2 class="mb-4">Manage Categories</h2>

    <!-- Add Form -->
    <form @submit.prevent="addCategory" class="row g-2 mb-4">
      <div class="col-md-8">
        <input v-model="newCategoryName" class="form-control" placeholder="New category name" required />
      </div>
      <div class="col-md-4">
        <button type="submit" class="btn btn-primary w-100">Add</button>
      </div>
    </form>

    <!-- Category List -->
    <ul class="list-group">
      <li
        v-for="cat in categories"
        :key="cat.id"
        class="list-group-item d-flex justify-content-between align-items-center"
      >
        <span>{{ cat.name }}</span>
        <ActionButtons
          @edit="() => openEditModal(cat)"
          @delete="() => confirmDelete(cat)"
        />
      </li>
    </ul>

    <!-- Edit Modal -->
    <EditModal
      v-if="editingCategory"
      :title="'Edit Category'"
      :model-value="editedName"
      @save="saveEdit"
      @cancel="cancelEdit"
      @update:model-value="editedName = $event"
    />

    <!-- Delete Modal -->
    <ConfirmModal
      v-if="categoryToDelete"
      :title="'Delete Category'"
      :message="`Are you sure you want to delete '${categoryToDelete.name}'?`"
      @confirm="deleteCategory"
      @cancel="cancelDelete"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategoryById
} from '@/services/categoryService'
import ActionButtons from '@/components/ActionButtons.vue'
import EditModal from '@/components/EditModal.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'

const categories = ref([])
const newCategoryName = ref('')
const editingCategory = ref(null)
const editedName = ref('')
const categoryToDelete = ref(null)

const loadCategories = async () => {
  categories.value = await getCategories()
}

const addCategory = async () => {
  if (!newCategoryName.value.trim()) return
  await createCategory({ name: newCategoryName.value.trim() })
  newCategoryName.value = ''
  await loadCategories()
}

const openEditModal = (category) => {
  editingCategory.value = category
  editedName.value = category.name
}

const saveEdit = async () => {
  if (!editedName.value.trim()) return
  await updateCategory(editingCategory.value.id, { name: editedName.value.trim() })
  editingCategory.value = null // Close modal
  await loadCategories()
}

const cancelEdit = () => {
  editingCategory.value = null
}

const confirmDelete = (category) => {
  categoryToDelete.value = category
}

const deleteCategory = async () => {
  await deleteCategoryById(categoryToDelete.value.id)
  categoryToDelete.value = null
  await loadCategories()
}

const cancelDelete = () => {
  categoryToDelete.value = null
}

onMounted(loadCategories)
</script>

