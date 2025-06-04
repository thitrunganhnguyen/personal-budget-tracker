import { Toast } from 'bootstrap'

export function showToast(message, variant = 'success') {
  const toastEl = document.getElementById('app-toast')
  const toastBody = document.getElementById('toast-body')

  // Text setzen
  toastBody.textContent = message

  // Farbe setzen (Bootstrap-Klassen)
  toastEl.className = `toast align-items-center text-white bg-${variant} border-0`

  // Zeigen
  const toast = new Toast(toastEl)
  toast.show()
}
