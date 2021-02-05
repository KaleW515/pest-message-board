import request from '@/util/request'

export const postReply = data => {
  return request({
    url: '/reply',
    method: 'POST',
    data
  })
}

export const getReply = (commentId, page) => {
  return request({
    url: `/reply/${commentId}/${page}`,
    method: 'GET'
  })
}

export const deleteReply = replyId => {
  return request({
    url: `/reply/${replyId}`,
    method: 'DELETE'
  })
}
