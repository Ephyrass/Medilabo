<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
      <div class="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
        <h3 class="text-xl font-bold text-gray-900">
          {{ isEdit ? 'Edit Patient' : 'Add New Patient' }}
        </h3>
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <form @submit.prevent="handleSubmit" class="p-6 space-y-6">
        <!-- Personal Information -->
        <div>
          <h4 class="text-lg font-semibold text-gray-900 mb-4">Personal Information</h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                First Name <span class="text-red-500">*</span>
              </label>
              <input
                v-model="form.firstName"
                type="text"
                required
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="John"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Last Name <span class="text-red-500">*</span>
              </label>
              <input
                v-model="form.lastName"
                type="text"
                required
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="Doe"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Date of Birth <span class="text-red-500">*</span>
              </label>
              <input
                v-model="form.birthDate"
                type="date"
                required
                :max="maxDate"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Gender <span class="text-red-500">*</span>
              </label>
              <select
                v-model="form.gender"
                required
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              >
                <option value="">Select gender</option>
                <option value="M">Male</option>
                <option value="F">Female</option>
              </select>
            </div>
          </div>
        </div>

        <!-- Contact Information -->
        <div>
          <h4 class="text-lg font-semibold text-gray-900 mb-4">Contact Information (Optional)</h4>
          <div class="grid grid-cols-1 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Phone Number
              </label>
              <input
                v-model="form.contactInfo.phoneNumber"
                type="tel"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="123-456-7890"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Address
              </label>
              <textarea
                v-model="form.contactInfo.address"
                rows="3"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="123 Main Street, City, State, ZIP"
              />
            </div>
          </div>
        </div>

        <!-- Actions -->
        <div class="flex justify-end space-x-3 pt-4 border-t">
          <button
            type="button"
            @click="$emit('close')"
            class="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 font-medium transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            class="px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg font-medium transition-colors shadow-md hover:shadow-lg"
          >
            {{ isEdit ? 'Update Patient' : 'Add Patient' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  patient: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'save'])

const form = ref({
  firstName: '',
  lastName: '',
  birthDate: '',
  gender: '',
  contactInfo: {
    phoneNumber: '',
    address: ''
  }
})

const isEdit = computed(() => !!props.patient?.id)

const maxDate = computed(() => {
  return new Date().toISOString().split('T')[0]
})

const handleSubmit = () => {
  const patientData = {
    firstName: form.value.firstName,
    lastName: form.value.lastName,
    birthDate: form.value.birthDate,
    gender: form.value.gender,
    contactInfo: {
      phoneNumber: form.value.contactInfo.phoneNumber,
      address: form.value.contactInfo.address
    }
  }
  if (isEdit.value) {
    patientData.id = props.patient.id
  }
  emit('save', patientData)
}

onMounted(() => {
  if (props.patient) {
    form.value = {
      firstName: props.patient.firstName,
      lastName: props.patient.lastName,
      birthDate: props.patient.birthDate,
      gender: props.patient.gender,
      contactInfo: {
        phoneNumber: props.patient.contactInfo?.phoneNumber || '',
        address: props.patient.contactInfo?.address || ''
      }
    }
  }
})
</script>

