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
