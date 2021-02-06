import request from '@/util/request'

export const freezeUser = id => {
  return request({
    url: `/user/freeze/${id}`,
    method: 'POST'
  })
}

export const unfreezeUser = id => {
  return request({
    url: `/user/unfreeze/${id}`,
    method: 'DELETE'
  })
}

export const getAllUsers = () => {
  return request({
    url: '/user/all',
    method: 'POST'
  })
}
