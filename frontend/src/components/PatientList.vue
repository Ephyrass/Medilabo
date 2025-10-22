<template>
  <div>
    <div v-if="loading" class="text-gray-600">Loading patients...</div>
    <div v-else>
      <ul class="space-y-4">
        <li v-for="p in patients" :key="p.id" class="p-4 bg-white rounded shadow">
          <div class="flex justify-between">
            <div>
              <div class="font-semibold">{{ p.firstName }} {{ p.lastName }}</div>
              <div class="text-sm text-gray-500">DOB: {{ p.birthDate }} • Gender: {{ p.gender }}</div>
            </div>
            <div class="text-sm text-gray-700">{{ p.phoneNumber || '—' }}</div>
          </div>
          <div class="mt-2 text-gray-600">{{ p.address || 'No address' }}</div>
        </li>
      </ul>
      <div v-if="patients.length === 0" class="text-gray-600 mt-4">No patients found.</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const patients = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await axios.get('/api/patients')
    patients.value = res.data
  } catch (err) {
    console.error('Failed to fetch patients', err)
  } finally {
    loading.value = false
  }
})
</script>

