import type { Metadata } from 'next';
import './globals.css';
import { Polkadot } from '@/components/Polkadot';

export const metadata: Metadata = {
  title: 'DB diagram visualization',
  description: 'Website that returns a diagram of your database.',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>
        {children}
        <div id="output" />
        <Polkadot />
      </body>
    </html>
  );
}
