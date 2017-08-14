# -*- coding: utf-8 -*-

import platform
import os
import subprocess
import multiprocessing
from multiprocessing.pool import ThreadPool
import collections
import shutil

from invoke import task

MAX_WORKER_NUM = 4

@task
def check_flatc(cxt):
    path = shutil.which("flatc")
    if path is None:
        print("Not found flatc")


@task
def compile_converter(cxt):
    result = subprocess.run(["/home/yichiuan/tools/cmake/bin/cmake",
                             "-DCMAKE_BUILD_TYPE=Release", "."])
    result = subprocess.run(["make"])
    result = subprocess.run(["make", "install"])

@task
def convert_all(cxt):
    Entry = collections.namedtuple('Entry', ['type', 'in_path', 'out_path'])

    input_dir = 'data'
    out_dir = 'out'

    entries = (Entry('moe', os.path.join(input_dir, 'pack'), os.path.join(out_dir, 'moe')),
               Entry('taiwan', os.path.join(input_dir, 'ptck'), os.path.join(out_dir, 'taiwan')),
               Entry('hakka', os.path.join(input_dir, 'phck'), os.path.join(out_dir, 'hakka')),
               )

    for entry in entries:
        do_convert(entry.type, entry.in_path, entry.out_path)


@task
def convert(cxt, _type, input, output):
    do_convert(_type, input, output)


def work(cmd):
    result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    if result.stdout:
        print(result.stdout.decode('utf-8'))
    if result.stderr:
        print(result.stderr.decode('utf-8'))


def do_convert(_type, input, output):

    if not _type in ('moe', 'taiwan', 'hakka', 'radical', 'category'):
        print("type error.")
        return

    if not os.path.exists(input):
        print(input + 'not found')
        return

    os.makedirs(output, 0o777, True)

    files = os.listdir(input)

    converter = get_converter()

    worker_num = get_worker_num()
    thread_pool = ThreadPool(worker_num)

    for file in files:

        input_path = os.path.join(input, file)
        output_path = os.path.join(output, os.path.splitext(file)[0] + '.bin')

        if file.startswith('@'):
            cmd = f"{converter} radical {input_path} {output_path}".split()

        elif file.startswith('='):
            cmd = f"{converter} category {input_path} {output_path}".split()
        else:
            cmd = f"{converter} {_type} {input_path} {output_path}".split()

        # print(cmd)
        thread_pool.apply_async(work, (cmd,))

    thread_pool.close()
    thread_pool.join()


def get_converter():
    converter_path = os.path.join('bin', 'x86_64-linux-gnu')
    # if 'Linux' in platform.system():
    #     if '64bit' in platform.architecture():
    #         converter_path = os.path.join('bin', 'linux-x86_64')

    return os.path.join(converter_path, 'moe-converter')


def get_worker_num():
    return MAX_WORKER_NUM if multiprocessing.cpu_count() > MAX_WORKER_NUM \
                          else multiprocessing.cpu_count() - 1
