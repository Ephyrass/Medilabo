<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-lg shadow-xl max-w-5xl w-full max-h-[90vh] flex flex-col">
      <div class="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center rounded-t-lg">
        <div>
          <h3 class="text-xl font-bold text-gray-900">
            Notes d'observation - {{ patient.firstName }} {{ patient.lastName }}
          </h3>
          <p class="text-sm text-gray-600 mt-1">
            Historique des notes médicales
          </p>
        </div>
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div class="flex-1 overflow-y-auto p-6">
        <!-- Success Message -->
        <div v-if="successMessage" class="bg-green-50 border border-green-200 rounded-lg p-4 mb-6 flex items-center">
          <svg class="w-5 h-5 text-green-600 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
          </svg>
          <div>
            <p class="text-sm font-medium text-green-800">{{ successMessage }}</p>
          </div>
        </div>

        <!-- Add Note Form -->
        <div class="bg-indigo-50 rounded-lg p-4 mb-6">
          <h4 class="font-semibold text-gray-900 mb-3 flex items-center">
            <svg class="w-5 h-5 mr-2 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Ajouter une nouvelle note
          </h4>
          <form @submit.prevent="addNote" class="space-y-3">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">
                Nom du praticien
              </label>
              <input
                v-model="newNote.authorName"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="Dr. Martin"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">
                Note d'observation <span class="text-red-500">*</span>
              </label>
              <textarea
                v-model="newNote.content"
                required
                rows="4"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="Saisir vos observations..."
              ></textarea>
            </div>
            <div class="flex justify-end">
              <button
                type="submit"
                :disabled="saving"
                class="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-2 rounded-lg font-semibold transition-colors shadow-md hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
              >
                <svg v-if="!saving" class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                <span v-if="saving">Enregistrement...</span>
                <span v-else>Enregistrer la note</span>
              </button>
            </div>
          </form>
        </div>

        <!-- Notes List -->
        <div>
          <h4 class="font-semibold text-gray-900 mb-4 flex items-center">
            <svg class="w-5 h-5 mr-2 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            Historique ({{ notes.length }} notes)
          </h4>

          <!-- Loading State -->
          <div v-if="loading" class="text-center py-12">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto"></div>
            <p class="text-gray-600 mt-4">Chargement des notes...</p>
          </div>

          <!-- Empty State -->
          <div v-else-if="notes.length === 0" class="text-center py-12 bg-gray-50 rounded-lg">
            <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            <h3 class="mt-4 text-lg font-medium text-gray-900">Aucune note</h3>
            <p class="mt-2 text-sm text-gray-500">Commencez par ajouter une première note d'observation.</p>
          </div>

          <!-- Notes Cards -->
          <div v-else class="space-y-4">
            <div
              v-for="note in notes"
              :key="note.id"
              class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
            >
              <div class="flex justify-between items-start mb-3">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
                    <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                  </div>
                  <div>
                    <p class="font-semibold text-gray-900">{{ note.authorName || 'Praticien' }}</p>
                    <p class="text-sm text-gray-500">{{ formatDate(note.createdAt) }}</p>
                  </div>
                </div>
                <button
                  @click="confirmDeleteNote(note)"
                  class="text-red-600 hover:bg-red-50 p-2 rounded-lg transition-colors"
                  title="Supprimer"
                >
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
              <div class="text-gray-700 whitespace-pre-wrap bg-gray-50 p-3 rounded-lg">
                {{ note.content }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Delete Confirmation -->
    <ConfirmModal
      v-if="showDeleteConfirm && noteToDelete"
      title="Supprimer la note"
      message="Êtes-vous sûr de vouloir supprimer cette note ? Cette action est irréversible."
      @confirm="deleteNote"
      @cancel="showDeleteConfirm = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import ConfirmModal from './ConfirmModal.vue'

const props = defineProps({
  patient: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'noteAdded'])

const notes = ref([])
const loading = ref(true)
const saving = ref(false)
const showDeleteConfirm = ref(false)
const noteToDelete = ref(null)
const successMessage = ref(null)

const newNote = ref({
  content: '',
  authorName: ''
})

const fetchNotes = async () => {
  loading.value = true
  try {
    const res = await axios.get(`/api/notes/patient/${props.patient.id}`)
    notes.value = res.data
  } catch (err) {
    console.error('Error fetching notes:', err)
    alert('Erreur lors du chargement des notes')
  } finally {
    loading.value = false
  }
}

const addNote = async () => {
  if (!newNote.value.content.trim()) {
    return
  }

  saving.value = true
  successMessage.value = null
  try {
    await axios.post('/api/notes', {
      patientId: props.patient.id,
      content: newNote.value.content,
      authorName: newNote.value.authorName || 'Praticien'
    })

    // Reset form
    newNote.value.content = ''
    newNote.value.authorName = ''

    // Refresh notes
    await fetchNotes()

    // Update risk assessment after note is added
    try {
      await axios.get(`/api/risk/${props.patient.id}`)
      successMessage.value = 'Note enregistrée et risque mis à jour avec succès!'
    } catch (err) {
      successMessage.value = 'Note enregistrée, mais la mise à jour du risque a rencontré une erreur.'
      console.warn('Note added but risk assessment update had an issue:', err)
    }

    // Clear success message after 3 seconds
    setTimeout(() => {
      successMessage.value = null
    }, 3000)

    emit('noteAdded')
  } catch (err) {
    console.error('Error adding note:', err)
    alert('Erreur lors de l\'ajout de la note')
  } finally {
    saving.value = false
  }
}

const confirmDeleteNote = (note) => {
  noteToDelete.value = note
  showDeleteConfirm.value = true
}

const deleteNote = async () => {
  try {
    await axios.delete(`/api/notes/${noteToDelete.value.id}`)
    await fetchNotes()
    showDeleteConfirm.value = false
    noteToDelete.value = null
  } catch (err) {
    console.error('Error deleting note:', err)
    alert('Erreur lors de la suppression de la note')
  }
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  fetchNotes()
})
</script>

