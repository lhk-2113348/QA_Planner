export async function fetchTasks() {
  const response = await fetch('/api/tasks')
  if (!response.ok) {
    throw new Error('QA 일정 목록 조회 실패')
  }
  return response.json()
}

export async function uploadPlanExcel(file) {
  const formData = new FormData()
  formData.append('file', file)

  const response = await fetch('/api/upload', {
    method: 'POST',
    body: formData,
  })

  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || '엑셀 업로드 실패')
  }

  return response.text()
}
