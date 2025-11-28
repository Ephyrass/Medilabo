<template>
  <div>
    <div v-if="loading" class="text-gray-600">Loading patients...</div>
    <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-lg p-4">
      <p class="text-red-800 font-semibold">Error Loading Patients</p>
      <p class="text-red-600 text-sm">{{ error }}</p>
      <p class="text-red-500 text-xs mt-2">Check that the backend server is running on http://localhost:8080</p>
    </div>
    <div v-else>
      <ul class="space-y-4">
        <li v-for="p in patients" :key="p.id" class="p-4 bg-white rounded shadow">
          <div class="flex justify-between">
            <div>
              <div class="font-semibold">{{ p.firstName }} {{ p.lastName }}</div>
              <div class="text-sm text-gray-500">DOB: {{ p.birthDate }} • Gender: {{ p.gender }}</div>
            </div>
            <div class="text-sm text-gray-700">{{ p.contactInfo?.phoneNumber || '—' }}</div>
          </div>
          <div class="mt-2 text-gray-600">{{ p.contactInfo?.address || 'No address' }}</div>
        </li>
      </ul>
      <div v-if="patients.length === 0" class="text-gray-600 mt-4">No patients found.</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { patientService } from '../services/api'

const patients = ref([])
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  try {
    const res = await patientService.getAllPatients()
    patients.value = Array.isArray(res.data) ? res.data : []
  } catch (err) {
    error.value = err.message || 'Failed to fetch patients'
    console.error('Failed to fetch patients', err)
  } finally {
    loading.value = false
  }
})
</script>

