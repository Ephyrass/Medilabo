<template>
  <div class="space-y-6">
    <!-- Actions Bar -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <div class="flex justify-between items-center">
        <div>
          <h2 class="text-2xl font-bold text-gray-900">Patients</h2>
          <p class="text-sm text-gray-600 mt-1">Manage patient records and information</p>
        </div>
        <button
          @click="openAddModal"
          class="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-lg font-semibold flex items-center space-x-2 transition-colors shadow-md hover:shadow-lg"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          <span>Add Patient</span>
        </button>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="bg-white rounded-lg shadow-sm p-12">
      <div class="flex flex-col items-center justify-center space-y-4">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
        <p class="text-gray-600">Loading patients...</p>
      </div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-lg p-6">
      <div class="flex items-center space-x-3">
        <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <div>
          <h3 class="text-red-800 font-semibold">Error loading patients</h3>
          <p class="text-red-600 text-sm">{{ error }}</p>
        </div>
      </div>
    </div>

    <!-- Patients List -->
    <div v-else>
      <div v-if="patients.length === 0" class="bg-white rounded-lg shadow-sm p-12">
        <div class="text-center">
          <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
          <h3 class="mt-4 text-lg font-medium text-gray-900">No patients found</h3>
          <p class="mt-2 text-sm text-gray-500">Get started by adding a new patient.</p>
        </div>
      </div>

      <div v-else class="grid gap-4">
        <div
          v-for="patient in patients"
          :key="patient.id"
          class="bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow p-6"
        >
          <div class="flex justify-between items-start">
            <div class="flex-1">
              <div class="flex items-center space-x-3">
                <div class="w-12 h-12 rounded-full bg-indigo-100 flex items-center justify-center">
                  <span class="text-indigo-600 font-bold text-lg">{{ getInitials(patient) }}</span>
                </div>
                <div>
                  <h3 class="text-lg font-semibold text-gray-900">
                    {{ patient.firstName }} {{ patient.lastName }}
                  </h3>
                  <div class="flex items-center space-x-4 mt-1 text-sm text-gray-600">
                    <span class="flex items-center">
                      <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                      </svg>
                      {{ formatDate(patient.birthDate) }} ({{ calculateAge(patient.birthDate) }} years)
                    </span>
                    <span class="flex items-center">
                      <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                      </svg>
                      {{ patient.gender === 'M' ? 'Male' : 'Female' }}
                    </span>
                  </div>
                </div>
              </div>

              <div class="mt-4 grid grid-cols-2 gap-4">
                <div v-if="patient.phoneNumber" class="flex items-center text-sm text-gray-600">
                  <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                  </svg>
                  {{ patient.phoneNumber }}
                </div>
                <div v-if="patient.address" class="flex items-center text-sm text-gray-600">
                  <svg class="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                  </svg>
                  {{ patient.address }}
                </div>
              </div>

              <div class="mt-4 pt-4 border-t border-gray-200">
                <div class="flex items-center justify-between">
                  <div class="flex items-center space-x-2">
                    <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span class="text-sm font-medium text-gray-700">Diabetes Risk:</span>
                  </div>
                  <div v-if="patient.riskAssessment && !patient.riskLoading">
                    <span
                      :class="getRiskBadgeClass(patient.riskAssessment.riskLevel)"
                      class="px-3 py-1 rounded-full text-xs font-semibold"
                    >
                      {{ formatRiskLevel(patient.riskAssessment.riskLevel) }}
                    </span>
                  </div>
                  <div v-else-if="patient.riskLoading" class="text-sm text-gray-500">
                    <span class="animate-pulse">Evaluating...</span>
                  </div>
                  <div v-else class="text-sm text-gray-400">
                    Not assessed
                  </div>
                </div>
                <div v-if="patient.riskAssessment && patient.riskAssessment.triggerCount > 0" class="mt-2 text-xs text-gray-600">
                  {{ patient.riskAssessment.triggerCount }} trigger(s) detected
                </div>
              </div>
            </div>

            <div class="flex space-x-2 ml-4">
              <button
                @click="openNotesModal(patient)"
                class="p-2 text-green-600 hover:bg-green-50 rounded-lg transition-colors"
                title="Notes"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </button>
              <button
                @click="openEditModal(patient)"
                class="p-2 text-indigo-600 hover:bg-indigo-50 rounded-lg transition-colors"
                title="Edit"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
              </button>
              <button
                @click="confirmDelete(patient)"
                class="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                title="Delete"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <PatientModal
      v-if="showModal"
      :patient="selectedPatient"
      @close="closeModal"
      @save="savePatient"
    />

    <PatientNotesModal
      v-if="showNotesModal && selectedPatientForNotes"
      :patient="selectedPatientForNotes"
      @close="closeNotesModal"
      @noteAdded="handleNoteAdded"
    />

    <ConfirmModal
      v-if="showDeleteConfirm && patientToDelete"
      title="Delete Patient"
      :message="`Are you sure you want to delete ${patientToDelete.firstName || ''} ${patientToDelete.lastName || ''}? This action cannot be undone.`"
      @confirm="deletePatient"
      @cancel="showDeleteConfirm = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import PatientModal from './PatientModal.vue'
import PatientNotesModal from './PatientNotesModal.vue'
import ConfirmModal from './ConfirmModal.vue'

const patients = ref([])
const loading = ref(true)
const error = ref(null)
const showModal = ref(false)
const selectedPatient = ref(null)
const showDeleteConfirm = ref(false)
const patientToDelete = ref(null)
const showNotesModal = ref(false)
const selectedPatientForNotes = ref(null)

const fetchPatients = async () => {
  loading.value = true
  error.value = null
  try {
    const res = await axios.get('/api/patients')
    patients.value = res.data

    // Fetch risk assessment for each patient (Sprint 3)
    await fetchRiskAssessments()
  } catch (err) {
    error.value = err.message || 'Failed to fetch patients'
    console.error('Error fetching patients:', err)
  } finally {
    loading.value = false
  }
}

const fetchRiskAssessments = async () => {
  // Fetch risk assessment for each patient in parallel
  const riskPromises = patients.value.map(async (patient) => {
    patient.riskLoading = true
    try {
      const response = await axios.get(`/api/risk/${patient.id}`)
      patient.riskAssessment = response.data
    } catch (err) {
      console.warn(`Could not fetch risk for patient ${patient.id}:`, err.message)
      patient.riskAssessment = null
    } finally {
      patient.riskLoading = false
    }
  })

  await Promise.all(riskPromises)
}

const openAddModal = () => {
  selectedPatient.value = null
  showModal.value = true
}

const openEditModal = (patient) => {
  selectedPatient.value = { ...patient }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  selectedPatient.value = null
}

const openNotesModal = (patient) => {
  selectedPatientForNotes.value = patient
  showNotesModal.value = true
}

const closeNotesModal = () => {
  showNotesModal.value = false
  selectedPatientForNotes.value = null
}

const handleNoteAdded = async () => {
  if (selectedPatientForNotes.value) {
    selectedPatientForNotes.value.riskLoading = true
    try {
      const response = await axios.get(`/api/risk/${selectedPatientForNotes.value.id}`)
      selectedPatientForNotes.value.riskAssessment = response.data

      const patientIndex = patients.value.findIndex(p => p.id === selectedPatientForNotes.value.id)
      if (patientIndex !== -1) {
        patients.value[patientIndex].riskAssessment = response.data
      }
    } catch (err) {
      console.warn('Could not refresh risk assessment:', err.message)
    } finally {
      selectedPatientForNotes.value.riskLoading = false
    }
  }
}

const savePatient = async (patientData) => {
  try {
    if (patientData.id) {
      await axios.put(`/api/patients/${patientData.id}`, patientData)
    } else {
      await axios.post('/api/patients', patientData)
    }
    await fetchPatients()
    closeModal()
  } catch (err) {
    console.error('Error saving patient:', err)
    alert('Failed to save patient: ' + (err.response?.data?.message || err.message))
  }
}

const confirmDelete = (patient) => {
  patientToDelete.value = patient
  showDeleteConfirm.value = true
}

const deletePatient = async () => {
  try {
    await axios.delete(`/api/patients/${patientToDelete.value.id}`)
    await fetchPatients()
    showDeleteConfirm.value = false
    patientToDelete.value = null
  } catch (err) {
    console.error('Error deleting patient:', err)
    alert('Failed to delete patient: ' + (err.response?.data?.message || err.message))
  }
}

const getInitials = (patient) => {
  if (!patient || !patient.firstName || !patient.lastName) {
    return '??'
  }
  return `${patient.firstName[0]}${patient.lastName[0]}`.toUpperCase()
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const calculateAge = (birthDate) => {
  const today = new Date()
  const birth = new Date(birthDate)
  let age = today.getFullYear() - birth.getFullYear()
  const monthDiff = today.getMonth() - birth.getMonth()
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    age--
  }
  return age
}

const getRiskBadgeClass = (riskLevel) => {
  const classes = {
    NONE: 'bg-green-100 text-green-800',
    BORDERLINE: 'bg-yellow-100 text-yellow-800',
    IN_DANGER: 'bg-orange-100 text-orange-800',
    EARLY_ONSET: 'bg-red-100 text-red-800',
    None: 'bg-green-100 text-green-800',
    Borderline: 'bg-yellow-100 text-yellow-800'
  }
  return classes[riskLevel] || 'bg-gray-100 text-gray-800'
}

const formatRiskLevel = (riskLevel) => {
  const labels = {
    NONE: 'No risk',
    BORDERLINE: 'Borderline Risk',
    IN_DANGER: 'In Danger',
    EARLY_ONSET: 'Early onset',
    None: 'No risk',
    Borderline: 'Borderline Risk'
  }
  return labels[riskLevel] || riskLevel
}

onMounted(() => {
  fetchPatients()
})
</script>

