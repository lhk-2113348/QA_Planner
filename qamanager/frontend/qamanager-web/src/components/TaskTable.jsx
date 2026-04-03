export default function TaskTable({ tasks }) {
  return (
    <div className="card">
      <h2>QA 일정 목록</h2>

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>계획서ID</th>
            <th>기능명</th>
            <th>담당자</th>
            <th>예정일</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          {tasks.length === 0 ? (
            <tr>
              <td colSpan="6">등록된 QA 일정이 없습니다.</td>
            </tr>
          ) : (
            tasks.map((task) => (
              <tr key={task.id}>
                <td>{task.id}</td>
                <td>{task.planId}</td>
                <td>{task.featureName}</td>
                <td>{task.assignee}</td>
                <td>{task.plannedDate}</td>
                <td>{task.status}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}
