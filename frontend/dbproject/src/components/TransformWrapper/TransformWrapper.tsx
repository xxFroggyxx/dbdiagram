'use client';
import { TransformComponent, TransformWrapper as ZPPTransformWrapper } from 'react-zoom-pan-pinch';

type TransformWrapperTypes = {
  children: React.ReactNode;
};

export function TransformWrapper({ children }: TransformWrapperTypes) {
  return (
    <ZPPTransformWrapper centerOnInit limitToBounds={false} minScale={0.4}>
      <TransformComponent>{children}</TransformComponent>
    </ZPPTransformWrapper>
  );
}
