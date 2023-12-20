// TODO: mermaid.render

'use client';
import mermaid from 'mermaid';
import { useEffect, useState } from 'react';
import { useMedia } from 'react-use';

const theme = {
  dark: {
    primaryColor: 'red',
    primaryTextColor: '#17202A',
    primaryBorderColor: '#7C0000',
    lineColor: '#F8B229',
    secondaryColor: '#006100',
    tertiaryColor: '#fff',
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

export const Diagram = ({ chart }: any) => {
  const isDarkMode = useMedia('(prefers-color-scheme: dark)', false);
  const [svgContent, setSvgContent] = useState('');

  const { primaryColor, primaryTextColor, primaryBorderColor, lineColor, secondaryColor, tertiaryColor } = isDarkMode
    ? theme['dark']
    : theme['light'];

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
            'theme': 'dark',
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
        // console.log(svg);
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
