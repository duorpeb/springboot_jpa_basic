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
   // 제목 input
  const title = document.getElementById('t');
   // 내용 input
  const content = document.getElementById('c');
   // 파일 삭제 버튼 
  let fileDelBtn = document.querySelectorAll('.file-x');

  // readonly 를 풀고
  title.readOnly = false;
  content.readOnly = false;

  // 파일 선택 버튼 생성 
  let fileAddBtn = document.createElement('button');
  fileAddBtn.setAttribute('type','button');
  fileAddBtn.setAttribute('id','trigger');
  fileAddBtn.classList.add('btn', 'btn-secondary');
  fileAddBtn.innerHTML = "파일 선택";
  

  // 실제 submit 기능을 하는 버튼 생성 
  let subModBtn = document.createElement('button');
  subModBtn.setAttribute('type','submit');
  subModBtn.setAttribute('id','regBtn');
  subModBtn.classList.add('btn','btn-primary');
  subModBtn.innerText = "수정 완료";

  // <form> 태그 (id=form) 의 가장 마지막 요소로 추가
  document.getElementById('form').appendChild(fileAddBtn);
  document.getElementById('form').appendChild(subModBtn);
  // document.getElementById('subModBtn').remove();
  document.getElementById('delBtn').remove();

  // 파일 삭제 버튼 활성화 
  for(let btn of fileDelBtn){
    btn.style.visibility = 'visible';
  }

  // 파일 선택 클릭 시  
  if(document.getElementById('trigger')){
    let trigger = document.getElementById('trigger');
    
    // 파일 선택 이벤트 리스너 - input file 활성화 
    trigger.addEventListener('click', () => {
      document.getElementById('file').click();
    })
  }

  // 파일 삭제 버튼 (fileDelBtn) 이벤트 리스너 
  document.querySelectorAll('.file-x').forEach(fileDelBtn => {
    fileDelBtn.addEventListener('click', () => {
      let uuid = fileDelBtn.dataset.uuid;
      // 확인
      console.log("uuid : " + uuid);
      
      fileDeleteToServer(uuid).then(result => {
        if(result == '1'){
          alert('파일 삭제 완료!');
          fileDelBtn.closest('li').remove();
        }
      })
    })
  })
})



// fileDeleteToServer(uuid) - DELETE FROM file where uuid = #{uuid}
async function fileDeleteToServer(uuid) {
  try {
    const url = `/board/fileDel?uuid=${uuid}`;

    const config = {
      method : 'delete'
    }

    const resp = await fetch(url, config);

    const result = resp.text();

    return result;

  } catch (error) {
    console.log(error);
  }
} 