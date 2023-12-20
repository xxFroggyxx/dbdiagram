'use client';
import { TransformComponent, TransformWrapper as ZPPTransformWrapper } from 'react-zoom-pan-pinch';

export function TransformWrapper({ children }: any) {
  return (
    <ZPPTransformWrapper centerOnInit limitToBounds={false}>
      <TransformComponent>{children}</TransformComponent>
    </ZPPTransformWrapper>
  );
}
