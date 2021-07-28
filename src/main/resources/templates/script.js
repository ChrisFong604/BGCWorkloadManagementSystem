const video = document.querySelector("video");
const start = document.getElementById("start");
const stopped = document.getElementById("stop");
let recorder,stream;

async function startRecording() {
    stream = await navigator.mediaDevices.getDisplayMedia({
      video: { mediaSource: "screen" }
    });
    recorder = new MediaRecorder(stream);
  
    const chunks = [];
    recorder.ondataavailable = e => chunks.push(e.data);
    recorder.onstop = e => {
      const completeBlob = new Blob(chunks, { type: chunks[0].type });
      video.src = URL.createObjectURL(completeBlob);
    };
  
    recorder.start();
  }
stopped.addEventListener("click", () => {
    stopped.setAttribute("disabled", true);
    start.removeAttribute("disabled");
  
    recorder.stop();
    stream.getVideoTracks()[0].stop();
  });

start.addEventListener("click", () => {
    start.setAttribute("disabled", true);
    stopped.removeAttribute("disabled");
  
    startRecording();
  });
  
