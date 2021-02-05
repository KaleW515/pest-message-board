import request from '@/util/request'

export const login = data => {
  return request({
    url: '/user/login',
    method: 'POST',
    data
  })
}

export const getCaptcha = () => {
  return request({
    url: '/user/captcha',
    method: 'GET',
    responseType: 'arraybuffer'
  })
}

export const getUserInfo = () => {
  return request({
    url: '/user/info',
    method: 'GET'
  })
}

export const updateSignature = data => {
  return request({
    url: '/user/signature',
    method: 'POST',
    data
  })
}

export const revisePassword = data => {
  return request({
    url: '/user/revise',
    method: 'POST',
    data
  })
}

export const getInfoByName = username => {
  return request({
    url: `/user/info/${username}`,
    method: 'GET'
  })
}
