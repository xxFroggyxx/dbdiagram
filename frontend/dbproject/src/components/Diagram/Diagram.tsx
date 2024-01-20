'use client';
import mermaid from 'mermaid';
import { useEffect, useState } from 'react';
import { useMedia } from 'react-use';

type DiagramTypes = {
	chart: string;
};

const THEME = {
	dark: {
		primaryColor: '#0369a1',
		primaryTextColor: '#000000',
		primaryBorderColor: '#ffffff',
		lineColor: '#bf1333',
		secondaryColor: '#666666',
		tertiaryColor: '#666666',
	},
	light: {
		primaryColor: '#0ea5e9',
		primaryTextColor: '#111827',
		primaryBorderColor: '#000',
		lineColor: '#F8B229',
		secondaryColor: '#006100',
		tertiaryColor: '#fff',
	},
};

export const Diagram = ({ chart }: DiagramTypes) => {
	const isDarkMode = useMedia('(prefers-color-scheme: dark)', false);
	const [svgContent, setSvgContent] = useState('');

	const {
		primaryColor,
		primaryTextColor,
		primaryBorderColor,
		lineColor,
		secondaryColor,
		tertiaryColor,
	} = isDarkMode ? THEME['dark'] : THEME['light'];

	useEffect(() => {
		mermaid.initialize({
			er: { useMaxWidth: false, fontSize: 16 },
		});
	}, []);

	useEffect(() => {
		const fetchSvg = async () => {
			try {
				const themedChart = `
        %%{
          init: {
            'theme': 'base',
            'themeVariables': {
              'primaryColor': '${primaryColor}',
              'primaryTextColor': '${primaryTextColor}',
              'primaryBorderColor': '${primaryBorderColor}',
              'lineColor': '${lineColor}',
              'secondaryColor': '${secondaryColor}',
              'tertiaryColor': '${tertiaryColor}'
            }
          }
        }%%
        ${chart}
        `;
				const { svg } = await mermaid.render('dest', themedChart);
				setSvgContent(svg);
			} catch (error) {
				console.log('Error:', error);
			}
		};

		fetchSvg();
	}, [isDarkMode]);

	return (
		<div className='w-screen h-screen grid place-items-center'>
			<pre
				className='text-transparent overflow-hidden select-none mx-auto  hover:cursor-pointer'
				dangerouslySetInnerHTML={{ __html: svgContent }}
			/>
		</div>
	);
};
