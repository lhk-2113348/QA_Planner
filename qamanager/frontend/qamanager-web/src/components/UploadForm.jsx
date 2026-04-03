import { useState } from 'react'
import { uploadPlanExcel } from '../api/taskApi'

export default function UploadForm({ onUploadSuccess }) {
  const [file, setFile] = useState(null)
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')

  async function handleSubmit(e) {
    e.preventDefault()

    if (!file) {
      setMessage('엑셀 파일을 선택해주세요.')
      return
    }

    try {
      setLoading(true)
      setMessage('')
      const result = await uploadPlanExcel(file)
      setMessage(result || '업로드 완료')
      setFile(null)
      onUploadSuccess()
    } catch (error) {
      setMessage(error.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card">
      <h2>개발계획서 업로드</h2>
      <p className="subtext">엑셀 업로드 후 주요기능 기준으로 QA 일정이 생성됩니다.</p>

      <form onSubmit={handleSubmit} className="upload-form">
        <input
          type="file"
          accept=".xlsx"
          onChange={(e) => setFile(e.target.files?.[0] || null)}
        />
        <button type="submit" disabled={loading}>
          {loading ? '업로드 중...' : '엑셀 업로드'}
        </button>
      </form>

      {message && <p className="message">{message}</p>}
    </div>
  )
}
