console.log("===================================== bdReg.js =====================================");

// 초기화
const fileZone = document.getElementById('fileZone');


// 실행 파일 막기, 10MB 이상 파일 사이즈 제한
const regExp = new RegExp("\.(exe|sh|bat|jar|dll|msi)$");
const maxSize = 1024 * 1024 * 5;

document.addEventListener('change', (e) => {
  if(e.target.id == 'file'){
    // multiple 은 파일 객체가 배열로 들어옴
    const fileObj = document.getElementById('file').files;
    console.log(fileObj);
    
    // 등록버튼 비활성화 풀기
    document.getElementById('regBtn').disabled = false;
    
    // 이전 잘못된 파일들 제거
    fileZone.innerHTML="";
    
    // print file's info
    let ul = `<ul class="list-group list-group-flush">`;
    
    // 모든 첨부파일이 통과하는지 확인하기 위한 초기화
    let isOk = 1;
    
    // print file's info
    for(let f of fileObj){
      // 파일 형식 및 크기 확인
      let isValid = fileValidation(f.name, f.size);
      // 통과 됐는지 확인
      isOk *= isValid;
      ul+= `<li class="list-group-item">`;
      ul += `<div class="mb-3">`;
      ul += `${isValid ? '<div class="fw-bold">업로드 가능</div>' :
        '<div class="fw-bold text-danger">업로드 불가능</div>'
      }`;
      
      ul+= `${f.name}`;
      ul+= `<span class="badge rounded-pill text-bg-${isValid ? 'success' : 'danger'}">
      ${(Math.ceil(f.size/1048576))}MB</span>`;
      ul += `</div>`;
      ul += `</li>`;
    }
    ul += `</ul>`;
    
    fileZone.innerHTML = ul;
    
    // 파일 형식과 크기가 조건을 만족하지 않는다면 등록 버튼 비활성화
    if(isOk == 0){ document.getElementById('regBtn').disabled = true; }
  }
})


// 파일 유효성 검사
function fileValidation(fileName, fileSize){
  if(regExp.test(fileName)) { return 0; }
  
  else if(fileSize > maxSize) { return 0; }
  
  else { return 1; }
}


// trigger 클릭 시 input type = file 을 선택되게 하는 코드
console.log(">>>>> trigger in <<<<<");

document.getElementById('trigger').addEventListener('click', () => {
  document.getElementById('file').click();
})