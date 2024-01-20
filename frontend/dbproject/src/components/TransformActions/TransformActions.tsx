'use client';
import { createPortal } from 'react-dom';
import { useControls } from 'react-zoom-pan-pinch';

export const TransformActions = () => {
	const { zoomIn, zoomOut, centerView, resetTransform } = useControls();

	return createPortal(
		<div className='absolute top-0 left-0 '>
			<div className='flex rounded-md p-2 m-4 gap-6 bg-sky-500 dark:bg-sky-700'>
				<button
					className='rounded-md p-2 m-2 text-lg font-bold bg-white border-solid border-2 border-black hover:bg-slate-300 shadow-md hover:shadow-slate-700'
					onClick={() => zoomIn()}
				>
					Zoom in
				</button>
				<button
					className='rounded-md p-2 m-2 text-lg font-bold bg-white border-solid border-2 border-black hover:bg-slate-300 shadow-md hover:shadow-slate-700'
					type='button'
					onClick={() => zoomOut()}
				>
					Zoom out
				</button>
				<button
					className='rounded-md p-2 m-2 text-lg font-bold bg-white border-solid border-2 border-black hover:bg-slate-300 shadow-md hover:shadow-slate-700'
					type='button'
					onClick={() => centerView()}
				>
					Center
				</button>
				<button
					className='rounded-md p-2 m-2 text-lg font-bold bg-white border-solid border-2 border-black hover:bg-slate-300 shadow-md hover:shadow-slate-700'
					type='button'
					onClick={() => resetTransform()}
				>
					Reset
				</button>
				<button
					className='rounded-md p-2 m-2 text-lg font-bold bg-white border-solid border-2 border-black hover:bg-slate-300 shadow-md hover:shadow-slate-700'
					onClick={() => window.location.reload()}
				>
					Try with new db
				</button>
			</div>
		</div>,
		document.querySelector('#output')!
	);
};
