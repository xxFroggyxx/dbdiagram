import { Diagram } from '@/components/Diagram';
import mermaid from 'mermaid';

import type { InferGetStaticPropsType, GetStaticProps } from 'next';

export const getStaticProps = (async (context) => {
  const res = await fetch('http://127.0.0.1:8080/api/v1/test');
  const data = await res.text();
  return { props: { data } };
}) satisfies GetStaticProps<{
  data: string;
}>;

export default function Home({ data }: InferGetStaticPropsType<typeof getStaticProps>) {
  mermaid.initialize({ startOnLoad: true });

  return (
    <main className="grid place-items-center min-h-screen">
      <Diagram data={data} />
    </main>
  );
}
