console.log("============================ boardDetail.js ==========================================");
// 초기화
const delBtn = document.getElementById('delBtn');
const modBtn = document.getElementById('modBtn');


// delBtn 을 클릭 시 게시글 삭제 
// delBtn.addEventListener('click', () => {
//   location.href = `/board/delete?bno=${bno}`;
// })

// modBtn 을 클릭 시 title, content 의 readonly 풀기 
modBtn.addEventListener('click', () => {
  // 초기화
  const title = document.getElementById('t');
  const content = document.getElementById('c');

  // readonly 를 풀고
  title.readOnly = false;
  content.readOnly = false;

  // 실제 submit 기능을 하는 버튼 생성 
  let subModBtn = document.createElement('button');
  subModBtn.setAttribute('type','submit');
  subModBtn.setAttribute('id','regBtn');
  subModBtn.classList.add('btn','btn-primary');
  subModBtn.innerText = "수정 완료";

  // <form> 태그 (id=form) 의 가장 마지막 요소로 추가 
  document.getElementById('form').appendChild(subModBtn);
  document.getElementById('subModBtn').remove();
  document.getElementById('delBtn').remove();
})