import { create } from 'zustand';
import { CartItem } from '@/types';

interface CartState {
  items: CartItem[];
  itemCount: number;
  setItems: (items: CartItem[]) => void;
  addItem: (item: CartItem) => void;
  updateItem: (id: number, quantity: number) => void;
  removeItem: (id: number) => void;
  clearCart: () => void;
}

export const useCartStore = create<CartState>((set) => ({
  items: [],
  itemCount: 0,
  setItems: (items: CartItem[]) =>
    set({
      items,
      itemCount: items.reduce((sum, item) => sum + item.quantity, 0),
    }),
  addItem: (item: CartItem) =>
    set((state) => {
      const existingItem = state.items.find((i) => i.id === item.id);
      if (existingItem) {
        const updatedItems = state.items.map((i) =>
          i.id === item.id ? { ...i, quantity: i.quantity + item.quantity } : i
        );
        return {
          items: updatedItems,
          itemCount: updatedItems.reduce((sum, i) => sum + i.quantity, 0),
        };
      }
      const newItems = [...state.items, item];
      return {
        items: newItems,
        itemCount: newItems.reduce((sum, i) => sum + i.quantity, 0),
      };
    }),
  updateItem: (id: number, quantity: number) =>
    set((state) => {
      const updatedItems = state.items.map((i) => (i.id === id ? { ...i, quantity } : i));
      return {
        items: updatedItems,
        itemCount: updatedItems.reduce((sum, i) => sum + i.quantity, 0),
      };
    }),
  removeItem: (id: number) =>
    set((state) => {
      const updatedItems = state.items.filter((i) => i.id !== id);
      return {
        items: updatedItems,
        itemCount: updatedItems.reduce((sum, i) => sum + i.quantity, 0),
      };
    }),
  clearCart: () =>
    set({
      items: [],
      itemCount: 0,
    }),
}));
