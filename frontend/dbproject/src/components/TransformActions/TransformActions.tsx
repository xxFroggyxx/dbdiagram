'use client';
import { createPortal } from 'react-dom';
import { useControls } from 'react-zoom-pan-pinch';

export const TransformActions = () => {
  const { zoomIn, zoomOut, centerView, resetTransform } = useControls();

  return createPortal(
    <div className="absolute top-0 left-0 ">
      <div className="flex gap-12 bg-black dark:bg-white">
        <button type="button" onClick={() => zoomIn()}>
          Zoom in
        </button>
        <button type="button" onClick={() => zoomOut()}>
          Zoom out
        </button>
        <button type="button" onClick={() => centerView()}>
          center
        </button>
        <button type="button" onClick={() => resetTransform()}>
          reset
        </button>
        <button type="button" onClick={() => window.location.reload()}>
          Try with new db
        </button>
      </div>
    </div>,
    document.querySelector('#output')!
  );
};
