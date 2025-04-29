<script lang="ts">
    import { goto } from '$app/navigation';
    import { onMount } from 'svelte';
    
    // --- 環境変数からAPIベースURLを取得 ---
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';
  
    // --- 型定義 ---
    type User = {
      id: number;
      name: string;
      address: string;
    };
  
    // --- 状態管理変数 ---
    let users: User[] = [];
    let userId = '';
    let userName = '';
    let currentPage = 1;
    const pageSize = 5;
    let sortKey: keyof User = 'id';
    let sortOrder: 'asc' | 'desc' = 'asc';
  
    // --- ページ初回ロード時に全件検索 ---
    onMount(() => {
      searchUsers();
    });
    
    // --- 検索 ---
    async function searchUsers() {
      const params = new URLSearchParams();
      if (userId) params.append('id', userId);
      if (userName) params.append('name_like', userName);
  
      const res = await fetch(`${API_BASE_URL}/users?${params}`);
      users = await res.json();
      currentPage = 1;
    }
  
    // --- 詳細画面遷移 ---
    function viewDetail(id: number) {
      goto(`/users/${id}`);
    }
  
    // --- ソート ---
    function sortBy(key: keyof User, order: 'asc' | 'desc') {
      sortKey = key;
      sortOrder = order;
    }
  
    // --- ソート適用後のリスト ---
    $: sortedUsers = [...users].sort((a, b) => {
      const aVal = a[sortKey];
      const bVal = b[sortKey];
      if (typeof aVal === 'number' && typeof bVal === 'number') {
        return sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
      }
      return sortOrder === 'asc'
        ? String(aVal).localeCompare(String(bVal))
        : String(bVal).localeCompare(String(aVal));
    });
  
    // --- ページネーション ---
    $: paginatedUsers = sortedUsers.slice((currentPage - 1) * pageSize, currentPage * pageSize);
    $: totalPages = Math.ceil(users.length / pageSize);
  </script>
  
  <h1>ユーザー検索</h1>
  
  <form on:submit|preventDefault={searchUsers} style="display: flex; align-items: center; gap: 10px; margin-bottom: 20px;">
    <label>ユーザーID:<input type="text" bind:value={userId} /></label>
    <label>ユーザー名:<input type="text" bind:value={userName} /></label>
    <button type="submit">検索</button>
    <button type="button" on:click={() => goto('/users/new')}>新規登録</button>
  </form>
  
  <table border="1" style="border-collapse: collapse; width: 100%;">
    <thead>
      <tr style="background-color: #eef;">
        <th>
            ID
            <button type="button" style="all: unset; cursor: pointer; color: {sortKey === 'id' && sortOrder === 'asc' ? 'black' : '#ccc'}" on:click={() => sortBy('id', 'asc')}>
              ▲
            </button>
            <button type="button" style="all: unset; cursor: pointer; color: {sortKey === 'id' && sortOrder === 'desc' ? 'black' : '#ccc'}" on:click={() => sortBy('id', 'desc')}>
              ▼
            </button>
          </th>
          <th>
            ユーザー名
            <button type="button" style="all: unset; cursor: pointer; color: {sortKey === 'name' && sortOrder === 'asc' ? 'black' : '#ccc'}" on:click={() => sortBy('name', 'asc')}>
              ▲
            </button>
            <button type="button" style="all: unset; cursor: pointer; color: {sortKey === 'name' && sortOrder === 'desc' ? 'black' : '#ccc'}" on:click={() => sortBy('name', 'desc')}>
              ▼
            </button>
          </th>          
        <th>住所</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      {#each paginatedUsers as user}
        <tr>
          <td>{user.id}</td>
          <td>{user.name}</td>
          <td>{user.address}</td>
          <td><button on:click={() => viewDetail(user.id)}>詳細</button></td>
        </tr>
      {/each}
      {#if paginatedUsers.length === 0}
        <tr>
          <td colspan="4" style="text-align: center;">検索結果がありません</td>
        </tr>
      {/if}
    </tbody>
  </table>
  
  <div style="margin-top: 10px;">
    {#if currentPage > 1}
      <button on:click={() => currentPage--}>前へ</button>
    {/if}
    {#if currentPage * pageSize < users.length}
      <button on:click={() => currentPage++}>次へ</button>
    {/if}
  </div>
  
  <div style="margin-top: 5px;">
    ページ {currentPage} / {totalPages}
  </div>
  