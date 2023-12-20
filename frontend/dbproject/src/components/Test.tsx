import React from 'react';
import mermaid from 'mermaid';

const Test = async ({ chart }: any) => {
  const { svg } = await mermaid.render('dest', chart);
  return (
    <pre
      id="dest"
      className="mermaid text-transparent overflow-hidden select-none mx-auto"
      dangerouslySetInnerHTML={{ __html: svg }}
    />
  );
};

export default Test;
