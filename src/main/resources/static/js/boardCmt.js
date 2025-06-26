console.log("============================ boardCmt.js ==========================================");

// 초기화
 // 댓글 등록 버튼 
const cmtAddBtn = document.getElementById('cmtAddBtn');
 // 댓글 출력 ul
const cmtUl = document.getElementById('cmtUl');
 // 댓글 입력 창 
const cmtContent = document.getElementById('cmtContent');
 // 댓글 작성자 
const cmtWriter = document.getElementById('cmtWriter');

// 댓글 더보기/수정/삭제 버튼의 이벤트 리스너 
document.addEventListener('click', (e) => {
  // 댓글 더보기 버튼
  if(e.target.id == 'moreBtn'){
    /** page 의 값은 String 으로 반환됨 
     * 
     * <div class="mb-3">
          <button
            type="button"
            id="moreBtn"
            class="btn btn-outline-info"
            data-page="1"
            style="visibility : hidden;"
          >더보기</button>
        </div>
     * 
     * */ 
    let page = parseInt(e.target.dataset.page);
    printCmtList(bno, page);
  }


  // 댓글 삭제 버튼  
  if(e.target.classList.contains('del')){
    let li = e.target.closest('li');
    let cno = li.dataset.cno;
    
    deleteCmtToServer(cno).then(result => {
      if(result == '1'){
        alert('댓글 삭제 완료!');
        printCmtList(bno);
      }
    })
  }
  
  
  // 댓글 수정 버튼 - 1) 모달창을 띄우기 위한 사전 작업 
  if(e.target.classList.contains('mod')){
    // 해당 버튼이 달린 댓글 아이템 (li) 가져오기
    let li = e.target.closest('li');
    // 수정을 위한 cno (PK) 가져오기 
    let cno = li.dataset.cno;

    // 모달창에 댓글 내용 전달 
    document.getElementById('cmtTextMod').value = li.querySelector('.comment-text').innerText;
    // 작성자 추출
    let modCmtWriter = li.querySelector(".username").innerText;
    // 모달창에 작성자 표시
    document.getElementById('exampleModalLabel').innerText
      = `${modCmtWriter}`;
    // 모달창에 data-cno 초기화
    document.getElementById('cmtModBtn').setAttribute('data-cno', cno);
  }

  
  // 댓글 수정 버튼 - 2) 모달창 수정 버튼 클릭 시 변경된 댓글 내용을 비동기로 전송
  if(e.target.id == 'cmtModBtn'){
    console.log("cmdModBtn clicked..!");

    // CommentController 에 전송할 cvo 객체 생성 
    let cvo = {
      cno : e.target.dataset.cno,
      content : cmtTextMod.value
    }

    // 비동기로 전송 
    updateCmtToServer(cvo).then(result => {
      if(result == '1'){
        alert('댓글 수정 완료!');
      }

      printCmtList(bno);
    })

    // 비동기로 전송 후 모달창 닫기 
    document.querySelector('.btn-close').click();
  }
})


// 게시물 상세 조회 시 댓글 출력 
document.addEventListener('DOMContentLoaded', () => {
  printCmtList(bno);
})


// 댓글 등록 버튼 이벤트 리스너 
cmtAddBtn.addEventListener('click', () => {
  console.log("============================ boardCmt.js =>> cmtAddBtn EventListener ==========================================");

  // 댓글창이 공백인 경우 처리 
  if(cmtAddBtn.value=''){
    alert("댓글을 입력해주세요!");
  }

  const cmtObj = {
    bno : bno,
    writer : cmtWriter.innerText,
    content : cmtText.value
  }
  console.log(cmtObj)

  // 비동기로 댓글 전송
  postCmtToServer(cmtObj).then(result => {
    if(result == '1'){
      alert("댓글 등록 완료!");
      printCmtList(bno);
    }
  })
})


// updateCmtToServer(bno) - UPDATE comment set content = #{content} WHERE cno = #{cno} 
async function updateCmtToServer(cvo) {
  try {
    const url = "/comment/update";

    const config = {
      method : 'PUT',
      headers : {
        'Content-Type' : 'application/json; charset=utf-8'
      },
      body : JSON.stringify(cvo)
    };

    const resp = await fetch(url, config);

    const result = await resp.text();

    return result;

  } catch (error) {
    console.log(error);
  }

  
}

// deleteCmtToServer(cno) - DELETE FROM comment WHERE cno = #{cno}
async function deleteCmtToServer(cno) {
  try {
    const url = `/comment/delete?cno=${cno}`;

    const config = {
      method : 'DELETE'
    }

    const resp = await fetch(url, config);

    const result = resp.text();

    return result;

  } catch (error) {
    console.log(error);
  }
}


// getCmtListFromServer() - SELECT * FROM comment WHERE bno = #{bno}
async function getCmtListFromServer(bno, page) {
  try {
    const url = `/comment/${bno}/${page}`;

    const resp = await fetch(url);

    // Page<CommentDTO> cvoList 형태로 return 
    const result = await resp.json();

    return result;

  } catch (error) {
    console.log(error);
  }
}


// postCmtToServer(cmtObj) - comment TABLE insert 비동기 작업 
async function postCmtToServer(cmtObj) {
  try {
    const url = '/comment/post';

    const config = {
      method : 'POST',
      headers : {
        'content-type' : 'application/json; charset=utf-8'
      },
      body : JSON.stringify(cmtObj)
    };

    const resp = await fetch(url, config);

    const result = await resp.text();

    return result;

  } catch (error) {
    console.log(error);
  }
} 


// printCmtList(bno, page) - 댓글 출력 
function printCmtList(bno, page=1){
  console.log("============================ boardCmt.js =>> printCmtList in ==========================================");

  getCmtListFromServer(bno,page).then(result => {
    console.log("============================ boardCmt.js =>> printCmtList =>> getCmtListFromServer in ==========================================");
    console.log(result);

    if(result.cvoList.length > 0){
      if(page == 1){ cmtUl.innerHTML = ''; }

      for(let cvo of result.cvoList){
        let li = `<li 
                      class="comment-item"
                      data-cno="${cvo.cno}">`;
  
            // 작성자, 등록일, 내용 출력
            li += `<div class="comment-body">`;
              // 작성자, 등록일 출력
              li += `<div class="comment-meta">`;
                li += `<span class="username">${cvo.writer}</span>`;
                li += `<span class="time">${cvo.regDate.substring(0,10)}</span>`;
              li += `</div>`; // fin 작성자 div
              // 내용 출력
              li += `<p class="comment-text">${cvo.content}</p>`;
            li += `</div>`;
  
            // 수정 및 삭제 버튼
              // 수정 버튼
            li += `<button 
                          type="button" 
                          class="btn btn-outline-primary mod"
                          style="width:45px; height:35px;"
                          data-bs-toggle="modal"
                          data-bs-target="#myModal"
                        >✏️</button>`;
              // 삭제 버튼
                li += `<button 
                          type="button" 
                          class="btn btn-outline-primary del"
                          style="width:45px; height:35px;"
                        >❌</button>`;
          li += `</li>`;

          cmtUl.innerHTML += li;
      } // for 문 fin

      // 더보기 버튼
      let moreBtn = document.getElementById('moreBtn');

      if(result.pageNo < result.ttcPage){
        moreBtn.style.visibility = 'visible';

        moreBtn.dataset.page = page + 1;
      } else {
        moreBtn.style.visibility = 'hidden';
      }
    } // 댓글이 없다면 (result.cvoList.length < 0);
      else { cmtUl.innerHTML = `<li class="list-group-item"> 댓글이 없습니다. </li>`; }
    

  })
}