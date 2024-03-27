'use client';
import dynamic from 'next/dynamic';
import { useCallback, useState } from 'react';
import { useDropzone } from 'react-dropzone';

import { Diagram } from '@/components/Diagram';
import { TransformWrapper } from '@/components/TransformWrapper';
const LazyTransformActions = dynamic(
	() =>
		import('@/components/TransformActions').then((mod) => mod.TransformActions),
	{
		ssr: false,
	}
);

export const UploadFile = () => {
	const [chart, setChart] = useState<string | null>(null);
	const [loading, setLoading] = useState(false);

	const handleFormSubmit = (file: File | Blob) => {
		setLoading(true);
		const formData = new FormData();
		formData.append('sqlFile', file);
		fetch('http://127.0.0.1:8080/api/v1/parser', {
			method: 'post',
			body: formData,
		})
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.text();
			})
			.then((data) => {
				setChart(data);
			})
			.catch((error) => {
				console.error('There was a problem with the fetch operation: ', error);
			})
			.finally(() => {
				setLoading(false);
			});
	};

	const onDrop = useCallback((acceptedFiles: File[] | Blob[]) => {
		if (acceptedFiles.length > 0) {
			handleFormSubmit(acceptedFiles[0]);
		}
	}, []);

	const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

	return (
		<div>
			{!chart && (
				<div className='flex items-center justify-center h-screen '>
					<div
						{...getRootProps()}
						className='border border-black dark:border-gray-300 p-4 w-96 h-32 grid place-items-center text-center border-dashed'
					>
						<input {...getInputProps()} />
						{loading ? (
							<p className='text-lg font-bold text-sky-500'>Ładowanie...</p>
						) : isDragActive ? (
							<p className='text-lg font-bold text-sky-500'>
								Upuść pliki tutaj...
							</p>
						) : (
							<p className='text-lg font-bold text-sky-500 dark:text-sky-700'>
								Przeciągnij i upuść pliki tutaj, lub kliknij, aby wybrać pliki
							</p>
						)}
					</div>
				</div>
			)}
			{chart && (
				<TransformWrapper>
					<Diagram chart={chart} />
					<LazyTransformActions />
				</TransformWrapper>
			)}
		</div>
	);
};
