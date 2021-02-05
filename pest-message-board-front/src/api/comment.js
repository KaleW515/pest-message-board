import request from '@/util/request'

export const getComment = data => {
  return request({
    url: '/comment',
    method: 'POST',
    data
  })
}

export const incLikeNum = commentId => {
  return request({
    url: `/like/${commentId}`,
    method: 'POST'
  })
}

export const incDislikeNum = commentId => {
  return request({
    url: `/dislike/${commentId}`,
    method: 'POST'
  })
}

export const getCommentByUserId = data => {
  return request({
    url: '/comment',
    method: 'POST',
    data
  })
}

export const publishComment = data => {
  return request({
    url: '/publish',
    method: 'POST',
    data
  })
}
