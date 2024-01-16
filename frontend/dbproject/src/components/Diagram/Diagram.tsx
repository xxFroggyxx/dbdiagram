'use client';
import mermaid from 'mermaid';
import { useEffect, useState } from 'react';
import { useMedia } from 'react-use';

type DiagramTypes = {
  chart: string;
};

const THEME = {
  dark: {
    primaryColor: '#1F2021',
    primaryTextColor: '#666666',
    primaryBorderColor: '#81B1DB',
    lineColor: '#F8B229',
    secondaryColor: '#666666',
    tertiaryColor: '#666666',
  },
  light: {
    primaryColor: 'red',
    primaryTextColor: '#28B463',
    primaryBorderColor: '#7C0000',
    lineColor: '#F8B229',
    secondaryColor: '#006100',
    tertiaryColor: '#fff',
  },
};

export const Diagram = ({ chart }: DiagramTypes) => {
  const isDarkMode = useMedia('(prefers-color-scheme: dark)', false);
  const [svgContent, setSvgContent] = useState('');

  const { primaryColor, primaryTextColor, primaryBorderColor, lineColor, secondaryColor, tertiaryColor } = isDarkMode
    ? THEME['dark']
    : THEME['light'];

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
    <div className="w-screen h-screen grid place-items-center">
      <pre
        className="text-transparent overflow-hidden select-none mx-auto"
        dangerouslySetInnerHTML={{ __html: svgContent }}
      />
    </div>
  );
};
