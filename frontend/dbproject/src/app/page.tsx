import dynamic from 'next/dynamic';
import { Diagram } from '../components/Diagram';
import { TransformWrapper } from '../components/TransformWrapper';
const LazyTransformActions = dynamic(() => import('../components/TransformActions'), { ssr: false });

async function getData() {
  const res = await fetch('http://127.0.0.1:8080/api/v1/test');

  if (!res.ok) {
    throw new Error('Failed to fetch data');
  }

  return res.text();
}

export default async function Home() {
  const chart = await getData();

  return (
    <>
      <TransformWrapper>
        <Diagram chart={chart} />
        <LazyTransformActions />
      </TransformWrapper>
    </>
  );
}
