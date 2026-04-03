import { useEffect, useState } from 'react'
import { fetchTasks } from '../api/taskApi'
import UploadForm from '../components/UploadForm'
import TaskTable from '../components/TaskTable'

export default function DashboardPage() {
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  async function loadTasks() {
    try {
      setLoading(true)
      setError('')
      const data = await fetchTasks()
      setTasks(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadTasks()
  }, [])

  return (
    <div className="page">
      <header className="page-header">
        <h1>QA 업무 자동화</h1>
        <p>개발계획서 업로드 → QA 일정 자동 생성 → 일정 목록 조회</p>
      </header>

      {error && <div className="error-box">{error}</div>}

      <UploadForm onUploadSuccess={loadTasks} />

      {loading ? <div className="card">불러오는 중...</div> : <TaskTable tasks={tasks} />}
    </div>
  )
}
