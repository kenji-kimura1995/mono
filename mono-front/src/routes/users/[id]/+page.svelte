<script lang="ts">
    import { page } from '$app/stores';
    import { goto } from '$app/navigation';
    import { onMount } from 'svelte';
  
    let userId: string = '';
    let user: { id?: number; name: string; address: string } = { name: '', address: '' };
    let isNew = false;
  
    $: userId = $page.params.id;
  
    onMount(async () => {
      if (userId === 'new') {
        isNew = true;
        return;
      }
      const res = await fetch(`http://localhost:8080/api/users/${userId}`);
      if (res.ok) {
        user = await res.json();
      } else {
        alert('ユーザーが見つかりません');
        goto('/users');
      }
    });
  
    async function saveUser() {
      if (!user.name.trim()) {
        alert('ユーザー名は必須です');
        return;
      }
      if (!user.address.trim()) {
        alert('住所は必須です');
        return;
      }
  
      if (isNew) {
        await fetch('http://localhost:8080/api/users', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(user)
        });
      } else {
        await fetch(`http://localhost:8080/api/users/${userId}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(user)
        });
      }
      goto('/users');
    }
  
    async function deleteUser() {
      if (confirm('本当に削除しますか？')) {
        await fetch(`http://localhost:8080/api/users/${userId}`, {
          method: 'DELETE'
        });
        goto('/users');
      }
    }
  </script>
  
  <h1>ユーザー{isNew ? '登録' : '詳細'}</h1>
  
  <form on:submit|preventDefault={saveUser} style="display: flex; flex-direction: column; gap: 10px; max-width: 400px;">
    <label>
      名前:
      <input type="text" bind:value={user.name} required />
    </label>
  
    <label>
      住所:
      <input type="text" bind:value={user.address} required />
    </label>
  
    <div style="display: flex; gap: 10px;">
      <button type="submit">{isNew ? '登録' : '更新'}</button>
  
      {#if !isNew}
        <button type="button" on:click={deleteUser}>削除</button>
      {/if}
  
      <button type="button" on:click={() => goto('/users')}>戻る</button>
    </div>
  </form>
  