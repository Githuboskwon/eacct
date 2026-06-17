import mitt from 'mitt';

/**
 * Vue 2의 `new Vue()` 이벤트버스를 mitt로 교체하면서 동작을 보존하기 위한 래퍼.
 *
 * Vue의 `$emit`은 각 리스너를 `invokeWithErrorHandling`으로 호출해 **리스너에서 던진
 * 예외를 격리**(콘솔 로그)하고 emit 호출부로 전파하지 않는다. 반면 mitt의 `emit`은
 * 예외를 그대로 호출부로 전파해, 기존에 조용히 넘어가던 리스너 내부 오류가
 * unhandled exception(개발 오버레이)으로 표면화된다.
 *
 * 마이그레이션 충실성을 위해, 여기서는 리스너를 **개별 try/catch**로 호출해
 * Vue와 동일하게 예외를 격리한다(한 리스너가 던져도 나머지 리스너는 계속 실행).
 */
export default function createBus() {
  const emitter = mitt();
  emitter.emit = (type, evt) => {
    [...(emitter.all.get(type) || [])].forEach((handler) => {
      try { handler(evt); } catch (e) { console.error('[eventBus] listener error:', e); }
    });
    [...(emitter.all.get('*') || [])].forEach((handler) => {
      try { handler(type, evt); } catch (e) { console.error('[eventBus] listener error:', e); }
    });
  };
  return emitter;
}
